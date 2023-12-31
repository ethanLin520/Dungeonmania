package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.playerState.BaseState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.entities.strategy.json.BattleableJson;
import dungeonmania.entities.strategy.move.PlayerMove;
import dungeonmania.entities.strategy.overlap.PlayerOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements Battleable {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    private Queue<Potion> potionQueue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;

    private PlayerState state;

    public Player(Position position, double health, double attack) {
        super(position);
        battleStatistics = new BattleStatistics(
                health,
                attack,
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
        inventory = new Inventory();
        state = new BaseState();
        setOverlapStrategy(new PlayerOverlap(this));
        setMoveStrategy(new PlayerMove(this));
        setJsonStrategy(new BattleableJson(this));
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public BattleItem getWeapon() {
        return inventory.getWeapon();
    }

    public boolean hasSceptre() {
        return inventory.hasSceptre();
    }

    public void setPotionQueue(Queue<Potion> queue) {
        this.potionQueue = queue;
    }

    public Queue<Potion> getPotionQueue() {
        return potionQueue;
    }

    public int getNextTrigger() {
        return nextTrigger;
    }
    
    public void setNextTrigger(int nextTrigger) {
        this.nextTrigger = nextTrigger;
    }

    public <T extends InventoryItem> T getFirst(Class<T> type) {
        return inventory.getFirst(type);
    }

    public List<String> getBuildables() {
        return inventory.getBuildables();
    }

    public boolean build(String entity, EntityFactory factory) {
        if (!inventory.getBuildables().contains(entity)) return false;
        InventoryItem item = inventory.doBuild(entity, factory);
        return inventory.add(item);
    }

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        getMoveStrategy().apply(map.getGame());
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public boolean pickUp(Entity item) {
        return inventory.add((InventoryItem) item);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Potion getEffectivePotion() {
        return inEffective;
    }

    public void setEffectivePotion(Potion inEffective) {
        this.inEffective = inEffective;
    }

    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null) inventory.remove(item);
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    private void triggerNextPotion(int currentTick) {
        if (potionQueue.isEmpty()) {
            inEffective = null;
            changeState(new BaseState());
            return;
        }
        inEffective = potionQueue.remove();
        inEffective.transitionState(this);
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public void changeState(PlayerState playerState) {
        state = playerState;
    }

    public void use(Potion potion, int tick) {
        inventory.remove(potion);
        potionQueue.add(potion);
        if (inEffective == null) {
            triggerNextPotion(tick);
        }
    }

    public void onTick(int tick) {
        if (inEffective == null || tick == nextTrigger) {
            triggerNextPotion(tick);
        }
    }

    public void remove(InventoryItem item) {
        inventory.remove(item);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public void setBattleStatistics(BattleStatistics newStat) {
        this.battleStatistics = newStat;
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, state.applyBuff());
    }

    @Override
    public double getHealth() {
        return getBattleStatistics().getHealth();
    }
}

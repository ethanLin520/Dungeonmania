package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.time.OlderPlayer;
import dungeonmania.entities.time.PastGame;
import dungeonmania.entities.time.TimeTurner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.map.GameMap;
import dungeonmania.map.GraphNode;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Game {
    private String id;
    private String name;
    private String config;
    private Goal goals;
    private GameMap map;
    private Player player;
    private BattleFacade battleFacade;
    private int initialTreasureCount;
    private EntityFactory entityFactory;
    private boolean isInTick = false;

    public static final int PLAYER_MOVEMENT = 0;
    public static final int PLAYER_MOVEMENT_CALLBACK = 1;
    public static final int AI_MOVEMENT = 2;
    public static final int AI_MOVEMENT_CALLBACK = 3;

    private int tickCount = 0;
    private PriorityQueue<ComparableCallback> sub = new PriorityQueue<>();
    private PriorityQueue<ComparableCallback> addingSub = new PriorityQueue<>();

    private int kills = 0;
    private int valuableCollect = 0;

    private List<Position> playerRoute = new ArrayList<>();
    private Map<Integer, Potion> potionTimeTable = new HashMap<>();
    private List<PastGame> pastGames = new ArrayList<>();

    public Game(String dungeonName) {
        this.name = dungeonName;
        this.map = new GameMap();
        this.battleFacade = new BattleFacade();
    }

    public void init() {
        this.id = UUID.randomUUID().toString();
        map.init();
        this.tickCount = 0;
        player = map.getPlayer();
        register(() -> player.onTick(tickCount), PLAYER_MOVEMENT, "potionQueue");
        initialTreasureCount = map.getEntities(Treasure.class).size();
        // Store game state for time travel purpose
        playerRoute.add(player.getPosition());
        pastGames.add(new PastGame(this));
    }

    public Game tick(Direction movementDirection) {
        registerOnce(
            () -> player.move(this.getMap(), movementDirection), PLAYER_MOVEMENT, "playerMoves");
        tick();
        return this;
    }

    public Game tick(String itemUsedId) throws InvalidActionException {
        Entity item = player.getEntity(itemUsedId);
        if (item == null)
            throw new InvalidActionException(String.format("Item with id %s doesn't exist", itemUsedId));
        if (!(item instanceof Bomb) && !(item instanceof Potion))
            throw new IllegalArgumentException(String.format("%s cannot be used", item.getClass()));

        registerOnce(() -> {
            if (item instanceof Bomb)
                player.use((Bomb) item, map);
            if (item instanceof Potion) {
                player.use((Potion) item, tickCount);
                potionTimeTable.put(tickCount, (Potion)item);
            }
        }, PLAYER_MOVEMENT, "playerUsesItem");
        tick();
        return this;
    }

    /**
     * Battle between player and enemy
     * @param player
     * @param enemy
     */
    public void battle(Player player, Enemy enemy) {
        battleFacade.battle(this, player, enemy);
        if (player.getHealth() <= 0) {
            map.destroyEntity(player);
        }
        if (enemy.getHealth() <= 0) {
            map.destroyEntity(enemy);
        }
    }

    public Game build(String buildable) throws InvalidActionException {
        List<String> buildables = player.getBuildables();
        if (!buildables.contains(buildable)) {
            throw new InvalidActionException(String.format("%s cannot be built", buildable));
        }
        registerOnce(() -> player.build(buildable, entityFactory), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public Game interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = map.getEntity(entityId);
        if (e == null || !(e instanceof Interactable))
            throw new IllegalArgumentException("Entity cannot be interacted");
        if (!((Interactable) e).isInteractable(player)) {
            throw new InvalidActionException("Entity cannot be interacted");
        }
        registerOnce(
            () -> ((Interactable) e).interact(player, this), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public <T extends Entity> long countEntities(Class<T> type) {
        return map.countEntities(type);
    }

    public void register(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id));
        else
            sub.add(new ComparableCallback(r, priority, id));
    }

    public void registerOnce(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id, true));
        else
            sub.add(new ComparableCallback(r, priority, id, true));
    }

    public void unsubscribe(String id) {
        for (ComparableCallback c : sub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
        for (ComparableCallback c : addingSub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
    }

    public int tick() {
        isInTick = true;
        sub.forEach(s -> s.run());
        isInTick = false;
        sub.addAll(addingSub);
        addingSub = new PriorityQueue<>();
        sub.removeIf(s -> !s.isValid());
        tickCount++;
        // update the weapons/potions duration

        // Store game state for time travel purpose
        playerRoute.add(player.getPosition());
        pastGames.add(new PastGame(this));

        return tickCount;
    }

    public int getTick() {
        return this.tickCount;
    }

    public void setTick(int tick) {
        this.tickCount = tick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfigName(String config) {
        this.config = config;
    }

    public Goal getGoals() {
        return goals;
    }

    public void setGoals(Goal goals) {
        this.goals = goals;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void setEntityFactory(EntityFactory factory) {
        entityFactory = factory;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BattleFacade getBattleFacade() {
        return battleFacade;
    }

    public void setBattleFacade(BattleFacade battleFacade) {
        this.battleFacade = battleFacade;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addOneKill() {
        this.kills++;
    }

    public int getInitialTreasureCount() {
        return initialTreasureCount;
    }

    public void setValuableCollect(int valuableCollect) {
        this.valuableCollect = valuableCollect;
    }

    public int getValuableCollect() {
        return valuableCollect;
    }

    public void collectOneValuable() {
        valuableCollect++;
    }

    public Game rewindGame(int tick, boolean turner) {
        if (turner && player.getInventory().getEntities(TimeTurner.class).size() < 1) throw new IllegalArgumentException("No Time Turner!");
        int targetTime = tickCount > tick ? tickCount - tick : 0;
        isInTick = !turner;
        restoreMap(pastGames.get(targetTime));
        isInTick = false;
        return this;
    }

    private void restoreMap(PastGame pastGame) {
        GameMap newMap = new GameMap();
        Map<Entity, Position> entities = pastGame.getEntityMap();
        for (Map.Entry<Entity, Position> entry: entities.entrySet()) {
            GraphNode newNode = new GraphNode(entry.getKey(), entry.getValue(), 1);

            if (newNode != null)
                newMap.addNode(newNode);
        }
        newMap.addNode(new GraphNode(player));
        newMap.setPlayer(player);
        newMap.setGame(this);
        setMap(newMap);
        OlderPlayer op = pastGame.getOP();
        register(() -> op.move(this), Game.AI_MOVEMENT, op.getId());
        op.setDisappearTick(tickCount);
    }

    public List<Position> getPlayerRoute() {
        return playerRoute;
    }

    public Map<Integer, Potion> getPotionTimeTable() {
        return potionTimeTable;
    }
}

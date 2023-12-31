package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.strategy.json.AlliedJson;
import dungeonmania.entities.strategy.move.AlliedMove;
import dungeonmania.entities.strategy.move.DijkstraMove;
import dungeonmania.entities.strategy.overlap.DefaultOverlap;
import dungeonmania.entities.strategy.overlap.EnemyOverlap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;

    private int mindControlStop = -1;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        setMoveStrategy(new DijkstraMove(this));
        setJsonStrategy(new AlliedJson(this));
    }

    public boolean isAllied() {
        return allied;
    }

    public void setAllied(boolean allied) {
        this.allied = allied;
    }

    public int getMindControlStop() {
        return mindControlStop;
    }

    public void setMindControlStop(int mindControlStop) {
        this.mindControlStop = mindControlStop;
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    protected boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * spend Treasure to bribe the merc
     */
    protected void spendCost(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void move(Game game) {
        if (game.getTick() == mindControlStop) {
            allied = false;
            setMoveStrategy(new DijkstraMove(this));
            setOverlapStrategy(new EnemyOverlap(this));
        }

        if (getMoveStrategy() instanceof DijkstraMove) {
            Position playerPos = game.getMap().getPlayer().getPosition();
            List<Position> cardinally = playerPos.getCardinallyAdjacentPositions();

            for (Position pos : cardinally) {
                if (pos.equals(getPosition()) && isAllied()) {
                    setMoveStrategy(new AlliedMove(this));
                }
            }
        }
        getMoveStrategy().apply(game);
    }


    @Override
    public void interact(Player player, Game game) {
        allied = true;
        if (player.hasSceptre()) {
            mindControlStop = game.getTick() + player.getFirst(Sceptre.class).getControlDuration();
        } else {
            spendCost(player);
        }
        setOverlapStrategy(new DefaultOverlap());
    }

    @Override
    public boolean isInteractable(Player player) {
        return (!allied && canBeBribed(player)) || player.hasSceptre();
    }
}

package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.strategy.move.AlliedMove;
import dungeonmania.entities.strategy.move.DijkstraMove;
import dungeonmania.entities.strategy.overlap.DefaultOverlap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        setMoveStrategy(new DijkstraMove(this));
    }

    public boolean isAllied() {
        return allied;
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
        getMoveStrategy().apply(game);

        if (getMoveStrategy() instanceof DijkstraMove) {
            Position playerPos = game.getMap().getPlayer().getPosition();
            List<Position> cardinally = playerPos.getCardinallyAdjacentPositions();
            
            for (Position pos : cardinally) {
                if (pos.equals(getPosition()) && isAllied()) {
                    setMoveStrategy(new AlliedMove(this));
                }
            }
        }
    } 


    @Override
    public void interact(Player player, Game game) {
        allied = true;
        spendCost(player);
        setOverlapStrategy(new DefaultOverlap());
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }
}

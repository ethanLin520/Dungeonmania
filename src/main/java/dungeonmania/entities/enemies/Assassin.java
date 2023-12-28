package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    public static final int DEFAULT_BRIBE_AMOUNT = 5;
    public static final double DEFAULT_ATTACK = 10.0;
    public static final double DEFAULT_HEALTH = 20.0;
    public final static double DEFAULT_BRIBE_FAIL_RATE = 0.5;

    private double failRate;

    public Assassin(Position position, double health, double attack, int bribeAmount, int bribeRadius, double failRate) {
        super(position, health, attack, bribeAmount, bribeRadius);
        this.failRate = failRate;
    }

    private boolean success() {
        return Math.random() >= failRate; // 0 <= Math.random() < 1
    }

    @Override
    public void interact(Player player, Game game) {
        // Determine whether can be successfully bribe
        if (player.hasSceptre() || success()) {
            super.interact(player, game);
        } else {
            // bribe failed but still cost
            spendCost(player);
        }
    }
}

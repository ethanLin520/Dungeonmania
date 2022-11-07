package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    public static final int DEFAULT_BRIBE_AMOUNT = 5;
    public static final double DEFAULT_ATTACK = 10.0;
    public static final double DEFAULT_HEALTH = 20.0;
    public final static double DEFAULT_BRIBE_FAIL_RATE = 0.5;
    private final static int PRECISION = 1000;
    private final static int SEED = 5;
    private final Random random = new Random(SEED);

    private double rate;

    public Assassin(Position position, double health, double attack, int bribeAmount, int bribeRadius, double rate) {
        super(position, health, attack, bribeAmount, bribeRadius);
        this.rate = rate;
    }

    @Override
    public void interact(Player player, Game game) {
        // Determine whether can be successfully bribe
        int upper = (int) Math.round(rate * PRECISION);
        if (random.nextInt(PRECISION) >= upper) {
            super.interact(player, game);
        } else {
            // bribe failed but still cost
            spendCost(player);
        }
    }
}

package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    private double rate;
    private final static int PRECISION = 1000;

    public Assassin(Position position, double health, double attack, int bribeAmount, int bribeRadius, double rate) {
        super(position, health, attack, bribeAmount, bribeRadius);
        this.rate = rate;
    }

    @Override
    public void interact(Player player, Game game) {
        // Determine whether can be successfully bribe
        Random random = new Random();
        int upper = (int) Math.round(rate * PRECISION);

        if (random.nextInt(PRECISION) < upper) {
            super.interact(player, game);
        } else {
            // bribe failed but still cost
            spendCost(player);
        }
    }
}

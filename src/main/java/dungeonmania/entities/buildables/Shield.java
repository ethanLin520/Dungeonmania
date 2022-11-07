package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.inventory.Inventory;

public class Shield extends Buildable {
    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            defence,
            1,
            1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    public boolean formula(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int keys = inventory.count(Key.class);
        int treasure = inventory.count(Treasure.class);

        if (wood >= 2 && (treasure >= 1 || keys >= 1)) {
            return true;
        }

        return false;
    }
}

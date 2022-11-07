package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.inventory.Inventory;

public class Bow extends Buildable  {

    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
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
            0,
            2,
            1));
    }

    @Override
    public int getDurability() {
        return durability;
    }


    public boolean formula(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int arrows = inventory.count(Arrow.class);

        if (wood >= 1 && arrows >= 3) {
            return true;
        }

        return false;
    }
}

package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.buildables.parts.AndParts;
import dungeonmania.entities.buildables.parts.BasicParts;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.inventory.Inventory;

public class Bow extends Buildable  {

    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
    }

    public Bow() {
        super(null);
        this.durability = 0;
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


    public boolean isBuildable(Inventory inventory) {
        return partsNeed.enough(inventory);
    }

    @Override
    public void logParts() {
        partsNeed = new AndParts(new BasicParts(Wood.class, 1), new BasicParts(Arrow.class, 3));
    }

    @Override
    public String getType() {
        return "bow";
    }

    @Override
    public Buildable build(EntityFactory factory, Inventory inventory) {
        if (!useParts(inventory)) return null;
        return factory.buildBow();
    }
}

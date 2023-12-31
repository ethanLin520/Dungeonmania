package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.buildables.parts.AndParts;
import dungeonmania.entities.buildables.parts.BasicParts;
import dungeonmania.entities.buildables.parts.OrParts;
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

    public Shield() {
        super(null);
        this.durability = 0;
        this.defence = 0;
    }

    public void setDurability(int durability) {
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
            defence,
            1,
            1));
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public void logParts() {
        partsNeed = new AndParts(
            new BasicParts(Wood.class, 2),
            new OrParts(
                new BasicParts(Key.class, 1),
                new BasicParts(Treasure.class, 1)
            )
        );
    }

    @Override
    public String getType() {
        return "shield";
    }

    @Override
    public Buildable build(EntityFactory factory, Inventory inventory) {
        useParts(inventory);
        return factory.buildShield();
    }
}

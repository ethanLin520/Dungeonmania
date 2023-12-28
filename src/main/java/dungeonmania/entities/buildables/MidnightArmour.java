package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.buildables.parts.AndParts;
import dungeonmania.entities.buildables.parts.BasicParts;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.inventory.Inventory;

public class MidnightArmour extends Buildable {
    private double extraAttack;
    private double extraDefence;

    public MidnightArmour(double extraAttack, double extraDefence) {
        super(null);
        this.extraAttack = extraAttack;
        this.extraDefence = extraDefence;
    }

    public MidnightArmour() {
        super(null);
        this.extraAttack = 0;
        this.extraDefence = 0;
    }

    public void setDurability(int durability) {
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            extraAttack,
            extraDefence,
            1,
            1));
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public int getDurability() {
        return 0;
    }

    @Override
    public void logParts() {
        partsNeed = new AndParts(new BasicParts(Sword.class, 1), new BasicParts(SunStone.class, 1));
    }

    @Override
    public String getType() {
        return "midnight_armour";
    }

    @Override
    public Buildable build(EntityFactory factory, Inventory inventory) {
        useParts(inventory);
        return factory.buildMidnighArmour();
    }
}

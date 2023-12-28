package dungeonmania.entities.buildables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.buildables.parts.BasicParts;
import dungeonmania.entities.buildables.parts.MultiParts;
import dungeonmania.entities.buildables.parts.OrParts;
import dungeonmania.entities.buildables.parts.Parts;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;

public class Sceptre extends Buildable {
    private int controlDuration;

    public Sceptre(int duration) {
        super(null);
        this.controlDuration = duration;
    }

    public Sceptre() {
        super(null);
        this.controlDuration = 0;
    }

    public void setDurability(int durability) {
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            0,
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
        List<Parts> parts = new ArrayList<>();
        parts.add(new OrParts(new BasicParts(Wood.class, 1), new BasicParts(Arrow.class, 2)));
        parts.add(new OrParts(new BasicParts(Treasure.class, 1), new BasicParts(Key.class, 1)));
        parts.add(new BasicParts(SunStone.class, 1));
        partsNeed = new MultiParts(parts);
    }

    @Override
    public String getType() {
        return "sceptre";
    }

    @Override
    public Buildable build(EntityFactory factory, Inventory inventory) {
        useParts(inventory);
        return factory.buildSceptre();
    }

    public int getControlDuration() {
        return controlDuration;
    }
}

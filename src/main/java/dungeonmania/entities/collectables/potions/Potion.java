package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Collectable;
import dungeonmania.entities.strategy.json.PotionJson;
import dungeonmania.util.Position;

public abstract class Potion extends Collectable {
    private int duration;

    public Potion(Position position, int duration) {
        super(position);
        this.duration = duration;
        setJsonStrategy(new PotionJson(this));
    }

    public int getDuration() {
        return duration;
    }

    public abstract BattleStatistics applyBuff(BattleStatistics origin);

    public abstract void transitionState(Player player);
}

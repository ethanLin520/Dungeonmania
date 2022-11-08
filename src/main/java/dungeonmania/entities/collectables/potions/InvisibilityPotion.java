package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;
import dungeonmania.entities.playerState.InvisibleState;


public class InvisibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvisibilityPotion(Position position, int duration) {
        super(position, duration);
        setType("invisibility_potion");
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                0,
                0,
                1,
                1,
                false,
                false));
    }

    public void transitionState(Player player) {
        player.changeState(new InvisibleState());
    }
}

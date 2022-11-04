package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;

public class BaseState implements PlayerState {
    @Override
    public BattleStatistics applyBuff() {
        return new BattleStatistics(
            0,
            0,
            0,
            1,
            1,
            false,
            true);
    }
}

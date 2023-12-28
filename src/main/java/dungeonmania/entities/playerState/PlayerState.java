package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;

public interface PlayerState {

    /**
     * @return the BattleStatistic of the buff of the current state
     */
    public BattleStatistics applyBuff();
}

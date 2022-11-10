package dungeonmania.entities.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.strategy.destroy.EnemyDestroy;
import dungeonmania.entities.strategy.move.OlderPlayerMove;
import dungeonmania.entities.strategy.overlap.EnemyOverlap;
import dungeonmania.util.Position;

public class OlderPlayer extends Enemy implements TimeTravel {
    private Map<Integer, Potion> potionTimeTable = new HashMap<>();
    private Potion inEffective = null;
    private List<Position> playerRoute = new ArrayList<>();
    private int atWhichTick;
    private int disappearTick;

    public OlderPlayer(Position position, double health, double attack, int atTick, List<Position> playerRoute, Map<Integer, Potion> potionTimeTable) {
        super(position, health, attack);
        atWhichTick = atTick;
        setMoveStrategy(new OlderPlayerMove(this));
        setOverlapStrategy(new EnemyOverlap(this));
        setDestroyStrategy(new EnemyDestroy(this));
        this.playerRoute = playerRoute;
        this.potionTimeTable = potionTimeTable;
    }

    @Override
    public boolean isAllied() {
        return false;
    }

    @Override
    public void move(Game game) {
        if (atWhichTick == disappearTick) {
            game.getMap().destroyEntity(this);
            return;
        }
        inEffective = potionTimeTable.getOrDefault(game.getTick(), null);
        super.move(game);
        atWhichTick++;
    }


    @Override
    public BattleStatistics getBattleStatistics() {
        if (inEffective == null)
            return super.getBattleStatistics();
        else
            return inEffective.applyBuff(super.getBattleStatistics());
    }

    public Position nextPos() {
        return playerRoute.get(atWhichTick);
    }

    public void setDisappearTick(int disappearTick) {
        this.disappearTick = disappearTick;
    }

}

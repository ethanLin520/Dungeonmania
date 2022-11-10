package dungeonmania.entities.enemies;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.strategy.destroy.EnemyDestroy;
import dungeonmania.entities.strategy.json.BattleableJson;
import dungeonmania.entities.strategy.overlap.EnemyOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable {
    private BattleStatistics battleStatistics;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(
                health,
                attack,
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
        setOverlapStrategy(new EnemyOverlap(this));
        setDestroyStrategy(new EnemyDestroy(this));
        setJsonStrategy(new BattleableJson(this));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public double getHealth() {
        return getBattleStatistics().getHealth();
    }

    public void setBattleStatistics(BattleStatistics newStat) {
        this.battleStatistics = newStat;
    }

    public abstract boolean isAllied();
}

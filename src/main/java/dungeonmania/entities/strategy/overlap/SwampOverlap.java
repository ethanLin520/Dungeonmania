package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.SwampTile;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;

public class SwampOverlap implements OverlapStrategy {
    private SwampTile swamp;

    public SwampOverlap(SwampTile swamp) {
        this.swamp = swamp;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Player) return;
        if (entity instanceof Enemy) {
            Enemy enemy = (Enemy) entity;
            if (enemy.isAllied()) return;
        }
        // Stuck the entity for movement_factor tick
        int nextTickMoving = map.getGame().getTick() + swamp.getMoveFactor();
        entity.onSwamp(nextTickMoving);
    }
}

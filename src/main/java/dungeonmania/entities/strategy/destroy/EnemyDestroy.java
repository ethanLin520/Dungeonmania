package dungeonmania.entities.strategy.destroy;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public class EnemyDestroy implements DestroyStrategy {
    private Entity enemy;

    public EnemyDestroy(Entity enemy) {
        this.enemy = enemy;
    }

    @Override
    public void apply(GameMap gameMap) {
        Game g = gameMap.getGame();
        g.unsubscribe(enemy.getId());
    }
}

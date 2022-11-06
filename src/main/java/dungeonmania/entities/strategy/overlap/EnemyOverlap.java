package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;

public class EnemyOverlap implements OverlapStrategy {
    private Enemy enemy;

    public EnemyOverlap(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, enemy);
        }
    }
}

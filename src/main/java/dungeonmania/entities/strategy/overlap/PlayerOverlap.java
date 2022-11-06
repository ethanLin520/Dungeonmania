package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;

public class PlayerOverlap  implements OverlapStrategy {
    private Player player;

    public PlayerOverlap(Player player) {
        this.player = player;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Enemy) {
            if (entity instanceof Mercenary) {
                if (((Mercenary) entity).isAllied()) return;
            }
            map.getGame().battle(player, (Enemy) entity);
        }
    }
}

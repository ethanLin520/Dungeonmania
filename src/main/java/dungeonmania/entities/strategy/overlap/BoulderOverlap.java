package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;

public class BoulderOverlap implements OverlapStrategy {
    private Boulder boulder;

    public BoulderOverlap(Boulder boulder) {
        this.boulder = boulder;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            map.moveTo(boulder, entity.getFacing());
        }
    }
}

package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface OverlapStrategy {
    public void apply(GameMap map, Entity entity);
}

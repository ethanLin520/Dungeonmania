package dungeonmania.entities.strategy.movedaway;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface MovedAwayStrategy {
    public void apply(GameMap map, Entity entity);
}

package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.strategy.overlap.CollectableOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Collectable extends Entity {
    public Collectable(Position position) {
        super(position);
        setOverlapStrategy(new CollectableOverlap(this));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}

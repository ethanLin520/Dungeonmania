package dungeonmania.entities;

import dungeonmania.entities.strategy.overlap.SwampOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Swamp extends Entity {
    private int moveFactor = 0;
    public Swamp(Position position, int moveFactor) {
        super(position.asLayer(Entity.FLOOR_LAYER));
        this.moveFactor = moveFactor;
        setOverlapStrategy(new SwampOverlap(this));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public int getMoveFactor() {
        return moveFactor;
    }
}

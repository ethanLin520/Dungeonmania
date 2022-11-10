package dungeonmania.entities.time;

import dungeonmania.entities.Entity;
import dungeonmania.entities.strategy.overlap.TimePortalOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends Entity {
    public static final int REWIND_TICK = 30;

    public TimeTravellingPortal(Position position) {
        super(position);
        setOverlapStrategy(new TimePortalOverlap());
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}

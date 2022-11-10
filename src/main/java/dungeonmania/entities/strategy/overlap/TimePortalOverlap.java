package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.time.TimeTravellingPortal;
import dungeonmania.map.GameMap;

public class TimePortalOverlap implements OverlapStrategy {
    private TimeTravellingPortal self;

    public TimePortalOverlap(TimeTravellingPortal self) {
        this.self = self;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        return;
    }
}

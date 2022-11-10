package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.time.TimeTravellingPortal;
import dungeonmania.map.GameMap;

public class TimePortalOverlap implements OverlapStrategy {
    public TimePortalOverlap() {
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            map.getGame().rewindGame(TimeTravellingPortal.REWIND_TICK, false);
        }
        return;
    }
}

package dungeonmania.entities.strategy.movedaway;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;

public class SwitchMovedAway implements MovedAwayStrategy {
    private Switch s;

    public SwitchMovedAway(Switch s) {
        this.s = s;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            Switch.deactivate(s);
        }
    }
}

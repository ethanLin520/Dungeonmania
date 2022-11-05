package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;

public class SwitchOverlap implements OverlapStrategy {
    private Switch thisSwitch;

    public SwitchOverlap(Switch thisSwitch) {
        this.thisSwitch = thisSwitch;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            Switch.activate(thisSwitch, map);
        }
    }
}

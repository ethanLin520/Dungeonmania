package dungeonmania.entities.time;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends Entity implements TimeTravel {
    public static final int REWIND_TICK = 30;

    public TimeTravellingPortal(Position position) {
        super(position);
    }
}

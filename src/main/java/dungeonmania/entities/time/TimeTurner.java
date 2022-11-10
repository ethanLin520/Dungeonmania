package dungeonmania.entities.time;

import dungeonmania.entities.collectables.Collectable;
import dungeonmania.util.Position;

public class TimeTurner extends Collectable implements TimeTravel {
    public TimeTurner(Position position) {
        super(position);
    }
}

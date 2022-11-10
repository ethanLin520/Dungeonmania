package dungeonmania.entities.collectables;

import dungeonmania.entities.Openable;
import dungeonmania.util.Position;

public class SunStone extends Valuable implements Openable {
    private int key = -1;

    public SunStone(Position position) {
        super(position);
    }

    public int getKey() {
        return key;
    }
}

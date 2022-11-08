package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

public class Treasure extends Valuable {
    public Treasure(Position position) {
        super(position);
    }

    @Override
    public boolean interchangeableWithSS() {
        return true;
    }
}

package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

public class Key extends Collectable {
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
        setType("key");
    }

    public int getnumber() {
        return number;
    }
}

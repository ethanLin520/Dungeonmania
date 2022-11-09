package dungeonmania.entities.collectables;

import dungeonmania.entities.Openable;
import dungeonmania.util.Position;

public class Key extends Collectable implements Openable{
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
    }

    public int getKey() {
        return number;
    }
}

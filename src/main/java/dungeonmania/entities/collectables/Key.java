package dungeonmania.entities.collectables;

import dungeonmania.entities.Openable;
import dungeonmania.entities.strategy.json.OpenableJson;
import dungeonmania.util.Position;

public class Key extends Collectable implements Openable{
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
        setJsonStrategy(new OpenableJson(this));
    }

    public int getKey() {
        return number;
    }

    @Override
    public boolean interchangeableWithSS() {
        return true;
    }
}

package dungeonmania.entities.buildables.parts;

import dungeonmania.entities.collectables.Collectable;
import dungeonmania.entities.inventory.Inventory;

public class BasicParts implements Parts {
    private Class<? extends Collectable> cls;
    private int num;

    public BasicParts(Class<? extends Collectable> cls, int num) {
        this.cls = cls;
        this.num = num;
    }

    @Override
    public boolean enough(Inventory inventory) {
        return inventory.count(cls) >= num;
    }
}

package dungeonmania.entities.buildables.parts;

import java.util.List;

import dungeonmania.entities.collectables.Collectable;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class BasicParts implements Parts {
    private Class<? extends InventoryItem> cls;
    private int num;

    public BasicParts(Class<? extends Collectable> cls, int num) {
        this.cls = cls;
        this.num = num;
    }

    @Override
    public boolean enough(Inventory inventory) {
        if (cls.equals(Key.class))
            return inventory.count(SunStone.class) + inventory.count(cls) >= num;
        return inventory.count(cls) >= num;
    }

    @Override
    public void use(Inventory inventory) {
        List<? extends InventoryItem> available = inventory.getEntities(cls);
        int numNeed = num;

        // Use Sun Stone to interchange Key
        if (cls.equals(Key.class)) numNeed -= inventory.count(SunStone.class);

        for (int i = 0; i < numNeed; i++) {
            InventoryItem item = available.get(i);
            if (!(item instanceof SunStone))
                inventory.remove(item);
        }
    }
}

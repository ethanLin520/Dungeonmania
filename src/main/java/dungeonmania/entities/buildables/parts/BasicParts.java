package dungeonmania.entities.buildables.parts;

import java.util.List;

import dungeonmania.entities.collectables.Collectable;
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
        return inventory.count(cls) >= num;
    }

    @Override
    public void use(Inventory inventory) {
        if (cls.equals(SunStone.class)) return;
        List<? extends InventoryItem> available = inventory.getEntities(cls);
        for (int i = 0; i < num; i++) {
            inventory.remove(available.get(i));
        }
    }
}

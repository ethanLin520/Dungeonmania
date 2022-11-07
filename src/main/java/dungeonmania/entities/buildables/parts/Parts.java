package dungeonmania.entities.buildables.parts;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.entities.inventory.InventoryItem;

public class Parts {
    private Map<Class<? extends InventoryItem>, Integer> spec = new HashMap<>();

    public void setAmount(Class<? extends InventoryItem> c, int amount) {
        spec.put(c, amount);
    }
}

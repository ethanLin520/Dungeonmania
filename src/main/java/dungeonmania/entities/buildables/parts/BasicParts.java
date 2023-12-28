package dungeonmania.entities.buildables.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class BasicParts implements Parts {
    private Class<? extends InventoryItem> cls;
    private final boolean ssEquivalent;
    private int num;

    public Class<? extends InventoryItem> getCls() {
        return cls;
    }

    public int getMinNum() {
        return num;
    }

    public BasicParts(Class<? extends InventoryItem> cls, int num) {
        this.cls = cls;
        this.num = num;
        this.ssEquivalent = cls.equals(Key.class) || cls.equals(Treasure.class);
    }

    @Override
    public boolean enough(Inventory inventory) {
        List<InventoryItem> availables = inventory.getInventoryItems();
        return remove(availables) != null;
    }

    @Override
    public boolean use(Inventory inventory) {
        List<InventoryItem> availables = inventory.getInventoryItems();
        if (availables == null || availables.size() < num) return false;

        List<InventoryItem> requireType = availables.stream().filter(cls::isInstance).collect(Collectors.toList());
        int i = 0;
        int size = requireType.size();
        for (; i < num && i < size; i++) {
            inventory.remove(requireType.remove(0));
        }

        // enough required type has been removed.
        if (i == num) return true;
        return false;
    }

    @Override
    public List<InventoryItem> remove(List<InventoryItem> availables) {
        if (availables == null || availables.size() < num) return null;
        List<InventoryItem> copy = new ArrayList<>(availables);

        List<InventoryItem> requireType = copy.stream().filter(cls::isInstance).collect(Collectors.toList());
        int i = 0;
        int size = requireType.size();
        for (; i < num && i < size; i++) {
            copy.remove(requireType.remove(0));
        }

        // enough required type has been removed.
        if (i == num) return copy;

        // not enough required type and no substitutable
        if (!ssEquivalent) return null;

        List<InventoryItem> ss = copy.stream().filter(SunStone.class::isInstance).collect(Collectors.toList());
        size = ss.size();
        for (; i < num && i < size; i++) {
            copy.remove(ss.remove(0));
        }
        if (i == num) return copy;
        else return null;
    }

    @Override
    public boolean containSS() {
        return cls.equals(SunStone.class);
    }
}

package dungeonmania.entities.buildables.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class MultiParts implements Parts {
    private List<Parts> partsList = new ArrayList<>();
    private int minNum = 0;

    public MultiParts(List<Parts> partsList) {
        this.partsList = partsList;
        this.partsList.sort(partsComparator);
        Collections.reverse(this.partsList);
        partsList.stream().forEach(p -> this.minNum += p.getMinNum());
    }

    @Override
    public boolean enough(Inventory inventory) {
        List<InventoryItem> copy = inventory.getInventoryItems();
        for (Parts p : partsList) {
            copy = p.remove(copy);
            if (copy == null) return false;
        }
        return true;
    }

    @Override
    public boolean use(Inventory inventory) {
        boolean res = true;
        for (Parts p : partsList) {
            if (!p.use(inventory)) res = false;
        }
        return res;
    }

    @Override
    public int getMinNum() {
        return minNum;
    }

    @Override
    public List<InventoryItem> remove(List<InventoryItem> availables) {
        if (availables == null || availables.size() <minNum) return null;
        List<InventoryItem> copy = new ArrayList<>(availables);
        for (Parts p : partsList) {
            copy = p.remove(copy);
            if (copy == null) return null;
        }
        return copy;
    }

    @Override
    public boolean containSS() {
        return partsList.stream().anyMatch(p -> p.containSS());
    }
}

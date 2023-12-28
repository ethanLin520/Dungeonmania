package dungeonmania.entities.buildables.parts;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class AndParts implements Parts {
    private Parts part1;
    private Parts part2;
    private int minNum;

    public AndParts(Parts part1, Parts part2) {
        this.part1 = part1;
        this.part2 = part2;
        this.minNum = part1.getMinNum() + part2.getMinNum();
    }

    @Override
    public boolean enough(Inventory inventory) {
        return remove(inventory.getInventoryItems()) != null;
    }

    @Override
    public boolean use(Inventory inventory) {
        return part1.use(inventory) && part2.use(inventory);
    }

    @Override
    public List<InventoryItem> remove(List<InventoryItem> availables) {
        if (availables == null || availables.size() < minNum) return null;
        List<InventoryItem> copy = new ArrayList<>(availables);
        copy = part1.remove(copy);
        copy = part2.remove(copy);
        return copy;
    }

    @Override
    public int getMinNum() {
        return minNum;
    }

    @Override
    public boolean containSS() {
        return part1.containSS() || part2.containSS();
    }
}

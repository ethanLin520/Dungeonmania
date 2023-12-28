package dungeonmania.entities.buildables.parts;

import java.util.List;
import java.util.Comparator;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public interface Parts {
    public int getMinNum();
    public boolean enough(Inventory inventory);

    /**
     * This does not modify availables.
     * @param availables - available to build the required parts
     * @return the list of items after the required parts have been removed.
     *          If not enough, return null.
     */
    public boolean use(Inventory inventory);

    /**
     * Remove the required parts, return the List after remove.
     * Does not modify availables.
     * @param availables
     * @return
     */
    public List<InventoryItem> remove(List<InventoryItem> availables);

    public boolean containSS();

    public static Comparator<Parts> partsComparator = new Comparator<Parts>() {
        @Override
        public int compare(Parts p1, Parts p2) {
            int num1 = 0;
            int num2 = 0;
            if (p1.containSS()) num1 = 1;
            if (p2.containSS()) num2 = 1;
            return num1 - num2;
        }
    };
}

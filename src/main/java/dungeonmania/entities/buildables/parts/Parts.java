package dungeonmania.entities.buildables.parts;

import dungeonmania.entities.inventory.Inventory;

public interface Parts {
    public boolean enough(Inventory inventory);

    /**
     * Assume there is enough item in the inventory.
     * Consume the parts required in the inventory.
     * @param inventory
     */
    public void use(Inventory inventory);
}

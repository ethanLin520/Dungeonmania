package dungeonmania.entities.buildables;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.buildables.parts.Parts;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem, BattleItem {
    protected Parts partsNeed;

    public Buildable(Position position) {
        super(position);
        logParts();
    }

    public boolean enoughParts(Inventory inventory) {
        return partsNeed.enough(inventory);
    }

    /**
     * Use the required parts to build a new instance of self.
     * @param inventory
     * @return whether parts is used successfully.
     */
    protected boolean useParts(Inventory inventory) {
        if (!enoughParts(inventory)) return false;
        partsNeed.use(inventory);
        return true;
    }

    public boolean isBuildable(Inventory inventory) {
        return partsNeed.enough(inventory);
    }

    public abstract void logParts();
    public abstract String getType();

    /**
     * Use the required parts in the inventory and return the buildable.
     * @param factory
     * @param inventory
     * @return null if not enough inventory is present, otherwise new Buildable.
     */
    public abstract Buildable build(EntityFactory factory, Inventory inventory);
}

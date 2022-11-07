package dungeonmania.entities.buildables;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
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

    public abstract boolean isBuildable(Inventory inventory);
    public abstract void logParts();
    public abstract String getType();
}

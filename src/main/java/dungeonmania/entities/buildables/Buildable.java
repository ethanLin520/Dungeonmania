package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem, BattleItem {
    protected Parts partsNeed = new Parts();

    public Buildable(Position position) {
        super(position);
    }

    public abstract boolean isBuildable(Inventory inventory);
    public abstract void logParts();
}

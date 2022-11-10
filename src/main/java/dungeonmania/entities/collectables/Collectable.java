package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.strategy.json.InventoryJson;
import dungeonmania.entities.strategy.overlap.CollectableOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Collectable extends Entity implements InventoryItem {
    public Collectable(Position position) {
        super(position);
        setOverlapStrategy(new CollectableOverlap(this));
        setJsonStrategy(new InventoryJson(this));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public boolean interchangeableWithSS() {
        return false;
    }
}

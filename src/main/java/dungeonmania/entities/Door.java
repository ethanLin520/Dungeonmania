package dungeonmania.entities;

import dungeonmania.map.GameMap;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.strategy.overlap.DoorOverlap;
import dungeonmania.util.Position;

public class Door extends Entity implements Openable{
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.number = number;
        setOverlapStrategy(new DoorOverlap(this));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player && hasKey((Player) entity));
    }

    public boolean hasKey(Player player) {
        Inventory inventory = player.getInventory();
        if (inventory.count(SunStone.class) > 0) return true;
        Key key = inventory.getFirst(Key.class);

        return (key != null && key.getKey() == number);
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public int getKey() {
        return number;
    }
}

package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.map.GameMap;

public class DoorOverlap implements OverlapStrategy {
    private Door door;

    public DoorOverlap(Door door) {
        this.door = door;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        Inventory inventory = player.getInventory();
        Key key = inventory.getFirst(Key.class);
        if (player.getInventory().count(SunStone.class) > 0) {
            door.open();
        } else if (door.hasKey(player)) {
            inventory.remove(key);
            door.open();
        }
    }
}

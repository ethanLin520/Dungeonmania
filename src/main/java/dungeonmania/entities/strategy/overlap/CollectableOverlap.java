package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Collectable;
import dungeonmania.map.GameMap;

public class CollectableOverlap implements OverlapStrategy {
    private Collectable collectable;

    public CollectableOverlap(Collectable collectable) {
        this.collectable = collectable;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(collectable)) return;
            map.destroyEntity(collectable);
        }
    }
}

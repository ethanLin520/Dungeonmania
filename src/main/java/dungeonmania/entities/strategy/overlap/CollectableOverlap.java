package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Collectable;
import dungeonmania.entities.collectables.Valuable;
import dungeonmania.entities.time.OlderPlayer;
import dungeonmania.map.GameMap;

public class CollectableOverlap implements OverlapStrategy {
    private Collectable collectable;

    public CollectableOverlap(Collectable collectable) {
        this.collectable = collectable;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            if (!p.pickUp(collectable)) return;
            map.destroyEntity(collectable);
            if (collectable instanceof Valuable)
                map.getGame().collectOneValuable();
        } else if (entity instanceof OlderPlayer) {
            map.destroyEntity(collectable);
        }
    }
}

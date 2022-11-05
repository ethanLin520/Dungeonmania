package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Portal;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.map.GameMap;

public class PortalOverlap implements OverlapStrategy {
    private Portal portal;

    public PortalOverlap(Portal portal) {
        this.portal = portal;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        if (portal.getPair() == null)
            return;
        if (entity instanceof Player || entity instanceof Mercenary || entity instanceof ZombieToast)
            Portal.doTeleport(portal, map, entity);
    }
}

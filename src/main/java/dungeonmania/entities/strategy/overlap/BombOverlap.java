package dungeonmania.entities.strategy.overlap;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Bomb.State;
import dungeonmania.map.GameMap;

public class BombOverlap implements OverlapStrategy {
    private Bomb bomb;

    public BombOverlap(Bomb bomb) {
        this.bomb = bomb;
    }

    @Override
    public void apply(GameMap map, Entity entity) {
        State state = bomb.getState();
        if (state != State.SPAWNED) return;
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(bomb)) return;
            Bomb.unlinkObserver(bomb);
            map.destroyEntity(bomb);
        }
        Bomb.setState(bomb, State.INVENTORY);
    }
}

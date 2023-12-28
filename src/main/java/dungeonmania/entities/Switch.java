package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.strategy.json.SwitchJson;
import dungeonmania.entities.strategy.movedaway.SwitchMovedAway;
import dungeonmania.entities.strategy.overlap.SwitchOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity {
    private boolean activated;
    private List<Bomb> bombs = new ArrayList<>();

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
        setOverlapStrategy(new SwitchOverlap(this));
        setMovedAwayStrategy(new SwitchMovedAway(this));
        setJsonStrategy(new SwitchJson(this));
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (activated) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Activate the given Switch s and notify all bombs subscribing.
     * @param s
     * @return true if successfully activated, false if already activated.
     */
    public static boolean activate(Switch s, GameMap map) {
        if (s.activated) return false;
        s.activated = true;
        s.bombs.stream().forEach(b -> b.notify(map));
        return true;
    }

    public static void deactivate(Switch s) {
        s.activated = false;
    }
}

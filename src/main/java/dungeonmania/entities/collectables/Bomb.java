package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Switch;
import dungeonmania.entities.strategy.overlap.BombOverlap;
import dungeonmania.map.GameMap;

public class Bomb extends Collectable {
    public enum State {
        SPAWNED,
        INVENTORY,
        PLACED
    }

    public static final int DEFAULT_RADIUS = 1;
    private State state;
    private int radius;

    private List<Switch> subs = new ArrayList<>();

    public Bomb(Position position, int radius) {
        super(position);
        state = State.SPAWNED;
        this.radius = radius;
        setOverlapStrategy(new BombOverlap(this));
    }

    public void subscribe(Switch s) {
        this.subs.add(s);
    }

    public void notify(GameMap map) {
        explode(map);
    }

    public void onPutDown(GameMap map, Position p) {
        setPosition(p);
        map.addEntity(this);
        this.state = State.PLACED;
        List<Position> adjPosList = getPosition().getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node)
                                        .stream()
                                        .filter(e -> (e instanceof Switch))
                                        .collect(Collectors.toList());
            entities.stream()
                    .map(Switch.class::cast)
                    .forEach(s -> s.subscribe(this, map));
            entities.stream()
                    .map(Switch.class::cast)
                    .forEach(s -> this.subscribe(s));
        });
    }

    /**
     * it destroys all entities in diagonally and cardinally adjacent cells, except for the player
     * @param map
     */
    public void explode(GameMap map) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                List<Entity> entities = map.getEntities(new Position(i, j));
                entities = entities.stream()
                    .filter(e -> !(e instanceof Player))
                    .collect(Collectors.toList());
                for (Entity e: entities) map.destroyEntity(e);
            }
        }
    }

    public State getState() {
        return state;
    }

    public static void setState(Bomb b, State s) {
        b.state = s;
    }

    public static void unlinkObserver(Bomb b) {
        b.subs.stream().forEach(s -> s.unsubscribe(b));
    }
}

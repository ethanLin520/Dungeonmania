package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.entities.strategy.destroy.DefaultDestroy;
import dungeonmania.entities.strategy.destroy.DestroyStrategy;
import dungeonmania.entities.strategy.json.JsonStrategy;
import dungeonmania.entities.strategy.json.DefaultJson;
import dungeonmania.entities.strategy.move.DefaultMove;
import dungeonmania.entities.strategy.move.MoveStrategy;
import dungeonmania.entities.strategy.move.SwampMove;
import dungeonmania.entities.strategy.movedaway.DefaultMovedAway;
import dungeonmania.entities.strategy.movedaway.MovedAwayStrategy;
import dungeonmania.entities.strategy.overlap.DefaultOverlap;
import dungeonmania.entities.strategy.overlap.OverlapStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.UUID;

import org.json.JSONObject;
public abstract class Entity {
    public static final int FLOOR_LAYER = 0;
    public static final int ITEM_LAYER = 1;
    public static final int DOOR_LAYER = 2;
    public static final int CHARACTER_LAYER = 3;

    private Position position;
    private Position previousPosition;
    private Position previousDistinctPosition;
    private Direction facing;
    private String entityId;

    private OverlapStrategy overlapStrategy = new DefaultOverlap();
    private MovedAwayStrategy movedAwayStrategy = new DefaultMovedAway();
    private DestroyStrategy destroyStrategy = new DefaultDestroy();
    private MoveStrategy moveStrategy = new DefaultMove();
    private JsonStrategy jsonStrategy = new DefaultJson(this);

    public Entity(Position position) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
        this.facing = null;
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }

    public final void onOverlap(GameMap map, Entity entity) {
        overlapStrategy.apply(map, entity);
    }

    public final void onMovedAway(GameMap map, Entity entity) {
        movedAwayStrategy.apply(map, entity);
    }

    public final void onDestroy(GameMap gameMap) {
        destroyStrategy.apply(gameMap);
    }

    public Position getPosition() {
        return position;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    public String getId() {
        return entityId;
    }

    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    public void setPosition(Direction direction) {
        setPosition(Position.translateBy(getPosition(), direction));
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public void move(Game game) {
        moveStrategy.apply(game);
    }

    public JSONObject toJson() {
        return jsonStrategy.apply();
    }

    protected final void setOverlapStrategy(OverlapStrategy overlapStrategy) {
        this.overlapStrategy = overlapStrategy;
    }

    protected final void setMovedAwayStrategy(MovedAwayStrategy movedAwayStrategy) {
        this.movedAwayStrategy = movedAwayStrategy;
    }

    protected final void setDestroyStrategy(DestroyStrategy destroyStrategy) {
        this.destroyStrategy = destroyStrategy;
    }

    protected final void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    protected final MoveStrategy getMoveStrategy() {
        return this.moveStrategy;
    }

    protected final void setJsonStrategy(JsonStrategy jsonStrategy) {
        this.jsonStrategy = jsonStrategy;
    }

    /**
     * Called when an entity move onto a swamp tile.
     * Sets the entity's move strategy to SwampMove.
     * @param nextMovingTick - next tick the entity can move off the swamp
     */
    public void onSwamp(int nextMovingTick) {
        setMoveStrategy(new SwampMove(this, nextMovingTick, getMoveStrategy()));
    }

    /**
     * Called when an entity moves off a swamp tile.
     * @param original - original move strategy before moving onto the swamp tile.
     */
    public void onMovedAwaySwamp(MoveStrategy original) {
        setMoveStrategy(original);
    }
}

package dungeonmania.entities;

import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.strategy.overlap.BoulderOverlap;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends Entity {

    public Boulder(Position position) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        setOverlapStrategy(new BoulderOverlap(this));
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (entity instanceof Spider) return false;
        if (entity instanceof Player && canPush(map, entity.getFacing())) return true;
        return false;
    }

    private boolean canPush(GameMap map, Direction direction) {
        Position newPosition = Position.translateBy(this.getPosition(), direction);
        for (Entity e : map.getEntities(newPosition)) {
            if (!e.canMoveOnto(map, this)) return false;
        }
        return true;
    }
}

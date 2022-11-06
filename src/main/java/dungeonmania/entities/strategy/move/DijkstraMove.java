package dungeonmania.entities.strategy.move;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class DijkstraMove implements MoveStrategy {
    private Entity entity;

    public DijkstraMove(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void apply(Game game) {
        GameMap map = game.getMap();
        Position currPos = entity.getPosition();
        Position nextPos = map.dijkstraPathFind(currPos, map.getPlayer().getPosition(), entity);
        map.moveTo(entity, nextPos);
    }
}

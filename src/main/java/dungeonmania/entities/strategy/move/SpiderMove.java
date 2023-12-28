package dungeonmania.entities.strategy.move;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;
import dungeonmania.entities.Boulder;

public class SpiderMove implements MoveStrategy{
    private List<Position> movementTrajectory;
    private int nextPositionElement;
    private boolean forward;

    private Spider spider;

    public SpiderMove(Spider spider, List<Position> movementTrajectory) {
        this.spider = spider;
        this.movementTrajectory = movementTrajectory;
        nextPositionElement = 1;
        forward = true;
    }

    private void updateNextPosition() {
        if (forward) {
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        } else {
            nextPositionElement--;
            if (nextPositionElement == -1) {
                nextPositionElement = 7;
            }
        }
    }

    public void apply(Game game) {
        Position nextPos = movementTrajectory.get(nextPositionElement);
        List<Entity> entities = game.getMap().getEntities(nextPos);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            updateNextPosition();
            updateNextPosition();
        }
        nextPos = movementTrajectory.get(nextPositionElement);
        entities = game.getMap().getEntities(nextPos);
        if (entities == null
                || entities.size() == 0
                || entities.stream().allMatch(e -> e.canMoveOnto(game.getMap(), spider))) {
            game.getMap().moveTo(spider, nextPos);
            updateNextPosition();
        }
    }
}

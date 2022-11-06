package dungeonmania.entities.strategy.move;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;


public class RandomMove implements MoveStrategy{
    private Enemy enemy;

    public RandomMove(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void apply(Game game) {
        GameMap map = game.getMap();

        List<Position> pos = enemy.getPosition().getCardinallyAdjacentPositions();
        pos = pos
            .stream()
            .filter(p -> map.canMoveTo(enemy, p)).collect(Collectors.toList());

        Position nextPos;
        if (pos.size() == 0) {
            nextPos = enemy.getPosition();
            map.moveTo(enemy, nextPos);
        } else {
            Random randGen = new Random();
            nextPos = pos.get(randGen.nextInt(pos.size()));
            map.moveTo(enemy, nextPos);
        }
    }
}

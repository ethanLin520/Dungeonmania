package dungeonmania.entities.strategy.move;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AlliedMove implements MoveStrategy{
    private Mercenary merc;

    public AlliedMove(Mercenary merc) {
        this.merc = merc;
    }

    @Override
    public void apply(Game game) {
        // Occupies the square the player was previous in
        GameMap map = game.getMap();
        Position nextPos = map.getPlayer().getPreviousDistinctPosition();
        map.moveTo(merc, nextPos);
    }
}

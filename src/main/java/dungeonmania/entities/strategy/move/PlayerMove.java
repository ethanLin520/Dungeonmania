package dungeonmania.entities.strategy.move;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;

public class PlayerMove implements MoveStrategy {
    private Player player;

    public PlayerMove(Player player) {
        this.player = player;
    }

    public void apply(Game game) {
        GameMap map = game.getMap();
        map.moveTo(player, player.getFacing());
    }
}

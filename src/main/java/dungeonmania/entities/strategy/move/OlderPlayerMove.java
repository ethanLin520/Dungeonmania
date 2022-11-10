package dungeonmania.entities.strategy.move;

import dungeonmania.Game;
import dungeonmania.entities.time.OlderPlayer;

public class OlderPlayerMove implements MoveStrategy {
    private OlderPlayer ogPlayer;

    public OlderPlayerMove(OlderPlayer ogPlayer) {
        this.ogPlayer = ogPlayer;
    }

    @Override
    public void apply(Game game) {
        game.getMap().moveTo(ogPlayer, ogPlayer.nextPos());
    }
}

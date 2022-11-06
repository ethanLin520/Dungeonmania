package dungeonmania.entities.strategy.move;

import dungeonmania.Game;

public class DefaultMove implements MoveStrategy{
    // DefualtMove ==> Don't move
    @Override
    public void apply(Game game) {
        return;
    }
}

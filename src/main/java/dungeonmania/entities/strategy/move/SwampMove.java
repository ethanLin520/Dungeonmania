package dungeonmania.entities.strategy.move;

import dungeonmania.Game;
import dungeonmania.entities.Entity;

public class SwampMove implements MoveStrategy {
    private Entity self;
    private int nextMovingTick;
    private MoveStrategy orginalMoveStrategy;

    public SwampMove(Entity self, int nextMovingTick, MoveStrategy originalMoveStrategy) {
        this.self = self;
        this.nextMovingTick = nextMovingTick;
        this.orginalMoveStrategy = originalMoveStrategy;
    }

    @Override
    public void apply(Game game) {
        if (game.getTick() == nextMovingTick) {
            self.onMovedAwaySwamp(orginalMoveStrategy);
        }
    }
}

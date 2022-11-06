package dungeonmania.entities.enemies.Spawners;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;
import dungeonmania.entities.strategy.destroy.EnemyDestroy;

public abstract class Spawner extends Entity{
    public Spawner(Position position) {
        super(position);
        setDestroyStrategy(new EnemyDestroy(this));
    }

    public abstract void spawn(Game game);
}

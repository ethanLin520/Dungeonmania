package dungeonmania.entities.enemies.Spawners;

import dungeonmania.Game;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends Spawner implements Interactable {
    public static final int DEFAULT_SPAWN_INTERVAL = 0;

    public ZombieToastSpawner(Position position, int spawnInterval) {
        super(position);
        setType("zombie_toast_spawner");
    }

    @Override
    public void spawn(Game game) {
        game.getEntityFactory().spawnZombie(game, this);
    }

    @Override
    public void interact(Player player, Game game) {
        player.getInventory().getWeapon().use(game);
        game.getMap().removeNode(this);
    }

    @Override
    public boolean isInteractable(Player player) {
        return Position.isAdjacent(player.getPosition(), getPosition()) && player.hasWeapon();
    }
}

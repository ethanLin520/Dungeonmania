package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Spawners.Spawner;

public class EnemyGoal implements Goal {
    private int targetKill;

    public EnemyGoal(int targetKill) {
        this.targetKill = targetKill;
    }

    @Override
    public boolean achieved(Game game) {
        Player player = game.getPlayer();
        if (player == null) return false;
        return player.getKills() >= targetKill && game.getMap().getEntities(Spawner.class).isEmpty();
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return ":enemies";
    }
}

package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Spawners.Spawner;

public class EnemyGoal implements Goal {
    private int targetKill;

    public EnemyGoal(int targetKill) {
        this.targetKill = targetKill;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        return game.getKills() >= targetKill && game.getMap().getEntities(Spawner.class).isEmpty();
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return ":enemies";
    }

    @Override
    public String goalType() {
        return "enemies";
    }
}

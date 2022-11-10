package dungeonmania.entities.time;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class PastGame {
    private int tick;
    private Map<Entity, Position> entityMap = new HashMap<>();
    OlderPlayer op;

    public PastGame(Game game) {
        this.tick = game.getTick();
        game.getMap().getEntities().forEach(e -> {
            Position pos = e.getPosition();
            if (e instanceof Player) {
                Player p = (Player) e;
                op = new OlderPlayer(
                    pos,
                    p.getHealth(),
                    p.getBattleStatistics().getAttack(),
                    game.getTick(),
                    game.getPlayerRoute(),
                    game.getPotionTimeTable()
                );
                entityMap.put(op, pos);
            } else {
                entityMap.put(e, pos);
            }
        });
    }

    public int getTick() {
        return tick;
    }

    public OlderPlayer getOP() {
        return op;
    }

    public Map<Entity, Position> getEntityMap() {
        return new HashMap<Entity, Position>(entityMap);
    }
}

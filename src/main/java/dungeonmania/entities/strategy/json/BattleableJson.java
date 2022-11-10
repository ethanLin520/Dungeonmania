package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;

public class BattleableJson extends DefaultJson {
    public BattleableJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        Battleable e = (Battleable) getEntity();
        JSONObject json = super.apply();
        JSONObject statJson = new JSONObject();

        BattleStatistics stat = e.getBattleStatistics();
        statJson.put("health", stat.getHealth());
        statJson.put("attack", stat.getAttack());
        statJson.put("defence", stat.getDefence());
        statJson.put("magnifier", stat.getMagnifier());
        statJson.put("reducer", stat.getReducer());
        statJson.put("invincible", stat.isInvincible());
        statJson.put("enabled", stat.isEnabled());

        json.put("stat", statJson);
        return json;
    }
}

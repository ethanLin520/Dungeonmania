package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Mercenary;

public class AlliedJson extends BattleableJson {
    public AlliedJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        Mercenary p = (Mercenary) getEntity();
        JSONObject json = super.apply();
        json.put("allied", p.isAllied());
        json.put("mind_control_stop", p.getMindControlStop());
        return json;
    }
}

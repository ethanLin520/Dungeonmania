package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.SwampTile;
import dungeonmania.entities.Entity;

public class SwampJson extends DefaultJson {
    public SwampJson(Entity entity) {
        super(entity);
    }
    
    public JSONObject apply() {
        SwampTile o = (SwampTile) getEntity();
        JSONObject json = super.apply();
        json.put("movement_factor", o.getMoveFactor());
        return json;
    }
}

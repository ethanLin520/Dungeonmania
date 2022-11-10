package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Openable;

public class OpenableJson extends DefaultJson {
    public OpenableJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        Openable o = (Openable) getEntity();
        JSONObject json = super.apply();
        json.put("key", o.getKey());
        return json;
    }
}

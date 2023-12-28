package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;

public class SwitchJson extends DefaultJson {
    public SwitchJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        JSONObject json = super.apply();
        Switch s = (Switch) getEntity();
        json.put("activated", s.isActivated());
        return json;
    }
}

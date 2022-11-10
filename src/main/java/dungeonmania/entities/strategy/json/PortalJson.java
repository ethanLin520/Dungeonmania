package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Portal;

public class PortalJson extends DefaultJson {
    public PortalJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        Portal p = (Portal) getEntity();
        JSONObject json = super.apply();
        json.put("colour", p.getColor().toUpperCase());
        json.remove("type");
        json.put("type", "portal");
        return json;
    }
}

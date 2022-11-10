package dungeonmania.entities.strategy.json;

import org.json.JSONObject;
import dungeonmania.entities.Entity;

public class InventoryJson extends DefaultJson {
    public InventoryJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        JSONObject json = super.apply();
        json.put("x",getEntity().getPosition().getX());
        json.put("y",getEntity().getPosition().getY());
        return json;
    }
}

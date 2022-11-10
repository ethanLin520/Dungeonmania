package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.Entity;

public interface JsonStrategy {
    public JSONObject apply();
    public Entity getEntity();
}

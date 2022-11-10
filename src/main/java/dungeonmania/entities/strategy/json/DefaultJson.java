package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.util.NameConverter;

public class DefaultJson implements JsonStrategy{
    private Entity entity;

    public DefaultJson(Entity entity) {
        this.entity = entity;
    }

    public JSONObject apply() {
        JSONObject json = new JSONObject();
        json.put("type", NameConverter.toSnakeCase(entity));
        json.put("entity-id", entity.getId());
        if (entity.getPosition() != null) {
            json.put("x", entity.getPosition().getX());
            json.put("y", entity.getPosition().getY());
        }
        
        return json;
    }

    public Entity getEntity() {
        return entity;
    }
}

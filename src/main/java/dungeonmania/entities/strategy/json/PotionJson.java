package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.potions.Potion;

public class PotionJson extends InventoryJson{
    public PotionJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        Potion b = (Potion) getEntity();
        JSONObject json = super.apply();
        json.put("duration", b.getDuration());
        return json;
    }
}

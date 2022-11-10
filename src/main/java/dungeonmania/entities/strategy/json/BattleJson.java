package dungeonmania.entities.strategy.json;

import org.json.JSONObject;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;


public class BattleJson extends InventoryJson {
    public BattleJson(Entity entity) {
        super(entity);
    }

    public JSONObject apply() {
        BattleItem b = (BattleItem) getEntity();
        JSONObject json = super.apply();
        json.put("durability", b.getDurability());
        return json;
    }
}

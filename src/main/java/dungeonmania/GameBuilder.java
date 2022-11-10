package dungeonmania;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.goals.Goal;
import dungeonmania.goals.GoalFactory;
import dungeonmania.map.GameMap;
import dungeonmania.map.GraphNode;
import dungeonmania.map.GraphNodeFactory;
import dungeonmania.util.FileLoader;

/**
 * GameBuilder -- A builder to build up the whole game
 * @author      Webster Zhang
 * @author      Tina Ji
 */
public class GameBuilder {
    private String configFile;
    private String dungeonFile;
    private String dungeonName;

    private JSONObject config = null;
    private JSONObject dungeon = null;

    public GameBuilder setConfigPath(String configName) {
        this.configFile = String.format("/configs/%s.json", configName);
        return this;
    }

    public GameBuilder setDungeonPath(String dungeonName) {
        this.dungeonName = dungeonName;
        this.dungeonFile = String.format("/dungeons/%s.json", dungeonName);
        return this;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public void setDungeon(JSONObject dungeon) {
        this.dungeon = dungeon;
    }

    public void setDungeonName(String dungeronName) {
        this.dungeonName = dungeronName;
    }

    public Game buildGame() {
        loadConfig();
        loadDungeon();
        if (dungeon == null && config == null) {
            return null; // something went wrong
        }

        Game game = new Game(dungeonName);
        EntityFactory factory = new EntityFactory(config);
        game.setEntityFactory(factory);
        buildMap(game);
        buildGoals(game);
        game.init();

        return game;
    }

    private void loadConfig() {
        try {
            if (config == null)
                config = new JSONObject(FileLoader.loadResourceFile(configFile));
        } catch (IOException e) {
            e.printStackTrace();
            config = null;
        }
    }

    private void loadDungeon() {
        try {
            if (dungeon == null)
                dungeon = new JSONObject(FileLoader.loadResourceFile(dungeonFile));
        } catch (IOException e) {
            dungeon = null;
        }
    }

    private void buildMap(Game game) {
        GameMap map = new GameMap();
        map.setGame(game);

        dungeon.getJSONArray("entities").forEach(e -> {
            JSONObject jsonEntity = (JSONObject) e;
            GraphNode newNode = GraphNodeFactory.createEntity(jsonEntity, game.getEntityFactory());
            Entity entity = newNode.getEntities().get(0);

            if (newNode != null)
                map.addNode(newNode);

            if (entity instanceof Player)
                map.setPlayer((Player) entity);
        });
        game.setMap(map);
    }

    public void buildGoals(Game game) {
        if (!dungeon.isNull("goal-condition")) {
            Goal goal = GoalFactory.createGoal(dungeon.getJSONObject("goal-condition"), config);
            game.setGoals(goal);
        }
    }

    public void buildInventory(Game game, JSONArray json, JSONObject config) {
        Inventory inventory = new Inventory();
        EntityFactory factory = new EntityFactory(config);

        for(int i = 0; i < json.length(); i++) {
            JSONObject item = json.getJSONObject(i);
            Entity e = factory.createEntity(item);
            inventory.add((InventoryItem) e);
        }
    }
}

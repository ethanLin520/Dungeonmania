package dungeonmania.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;


import dungeonmania.Game;
import dungeonmania.GameBuilder;
import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.goals.ComplexGoal;
import dungeonmania.goals.Goal;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;

public class GameSaver {
    private static final String SAVE_PATH = "src/main/resources/saves";
    private static final String SAVE_CONFIG = "/saves/config.json";

    private Game game = null;
    private JSONObject gameJson = null;

    public GameSaver(Game game) {
        this.game = game;
        this.gameJson = toJson();
        gameJson.put("tick", game.getTick());
    }

    /**
     * Return a json file's content.
     * @param file
     * @return String: json format.
     */
    private static String loadJsonFile(String file) {
        System.out.println("Loading file at: " + file);
        String load = null;

        try {
            load = FileLoader.loadResourceFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            load = null;
        }

        return load;
    }

    /**
     * Write a game to the save path.
     * @param name
     * @param json
     * @return True if succeed else fals
     */
    private static boolean writeJsonFile(String name, JSONObject json) {

        try {
            String savePath = SAVE_PATH + "/" + name;
            File checkFile = new File(savePath);

            if (checkFile.exists()) {
                // If file exists write to the file
                FileWriter file = new FileWriter(savePath);
                file.write(json.toString());
                file.flush();
                file.close();
            } else {
                // Else create a new file
                System.out.println("File doesn't exist creating new file: " + savePath);
                if (!checkFile.createNewFile()) {
                    throw new IOException("Failed to create new file: " + savePath);
                }
                writeJsonFile(name, json);
            }

            return true;
        } catch (IOException e) {
            System.out.println("Failed to save JSON file");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Add a new saveRecord to the config file in saves
     * @param dungeonConfig
     * @return
     */
    private static boolean addToSaveConfig(JSONObject saveRecord) {
        JSONArray saveConfig = loadSaveRecord();

        int oldConfig = -1;
        for (int i = 0; i < saveConfig.length(); i++) {
            JSONObject save = saveConfig.getJSONObject(i);
            if (save.getString("save-name").equals(saveRecord.getString("save-name"))) {
                oldConfig = i;
            }
        }

        if (oldConfig != -1) {
            saveConfig.remove(oldConfig);
        }
        saveConfig.put(saveRecord);

        JSONObject config = new JSONObject();
        config.put("saves", saveConfig);


        return writeJsonFile("config.json", config);
    }

    /**
     * Creats a new saveRecord
     * @param saveName
     * @param dungeon
     * @param config
     * @return JSONObject
     */
    private static JSONObject createRecord(String saveName, String dungeon, String config) {
        JSONObject o = new JSONObject();

        o.put("save-name", saveName);
        o.put("dungeon", dungeon);
        o.put("config", config);

        return o;
    }

    private static List<InventoryItem> loadInventoryJson(EntityFactory factory, JSONArray inv) {
        List<InventoryItem> inventories = new ArrayList<>();
        for (int i = 0; i < inv.length(); i++) {
            JSONObject json = inv.getJSONObject(i);
            InventoryItem item = (InventoryItem) factory.createEntity(json);
            inventories.add(item);
        }

        return inventories;
    }

    private static Queue<Potion> loadPotionQueue(EntityFactory factory, JSONArray queue) {
        Queue<Potion> potionQueue = new LinkedList<>();
        for (int i = 0; i < queue.length(); i++) {
            JSONObject json = queue.getJSONObject(i);
            potionQueue.add((Potion) factory.createEntity(json));
        }

        return potionQueue;
    }

    /**
     * Create a JSONObject for the game goals
     * @param goal
     * @return
     */
    private JSONObject createJsonGoal(Goal goal) {
        JSONObject json = new JSONObject();
        json.put("goal", goal.goalType());

        if (goal instanceof ComplexGoal) {
            JSONArray subgoals = new JSONArray();

            ComplexGoal cgoal = (ComplexGoal) goal;
            for (Goal subgoal : cgoal.getSubgoal()) {
                subgoals.put(createJsonGoal(subgoal));
            }

            json.put("subgoals", subgoals);
        }

        return json;
    }

    private JSONArray inventoryJson(Inventory inv) {
        JSONArray things = new JSONArray();

        List<InventoryItem> items = inv.getInventoryItems();
        for (InventoryItem item : items) {
            Entity thing = (Entity) item;
            things.put(thing.toJson());
        }

        return things;
    }

    private JSONArray potionQueueJson(Queue<Potion> potionQueue) {
        JSONArray queue = new JSONArray();
        for (Potion p : potionQueue) {
            queue.put(p.toJson());
        }
        return queue;
    }

    private JSONArray roundResponse(List<RoundResponse> rsps) {
        JSONArray json = new JSONArray();

        for (RoundResponse rsp : rsps) {
            JSONObject rspJson = new JSONObject();
            rspJson.put("delta-player-health", rsp.getDeltaCharacterHealth());
            rspJson.put("delta-enemy-health", rsp.getDeltaEnemyHealth());

            json.put(rspJson);
        }

        return json;
    }

    private JSONArray battleItemJson(List<ItemResponse> rsps) {
        JSONArray json = new JSONArray();

        for (ItemResponse rsp : rsps) {
            JSONObject rspJson = new JSONObject();
            rspJson.put("id", rsp.getId());
            rspJson.put("type", rsp.getType());
            
            json.put(rspJson);
        }
        return json;
    }

    private JSONArray battleRspJson(List<BattleResponse> rsps) {
        JSONArray json = new JSONArray();

        for (BattleResponse rsp : rsps) {
            JSONObject rspJson = new JSONObject();
            rspJson.put("enemy", rsp.getEnemy());
            rspJson.put("initial-player-health", rsp.getInitialPlayerHealth());
            rspJson.put("initial-enemy-health", rsp.getInitialEnemyHealth());
            
            JSONArray battleItems = battleItemJson(rsp.getBattleItems());
            rspJson.put("battle-items", battleItems);
            JSONArray rounds = roundResponse(rsp.getRounds());
            rspJson.put("rounds", rounds);

            json.put(rspJson);
        }

        return json;
    }

    private static RoundResponse loadRoundResponse(JSONObject roundJson) {
        RoundResponse rsp = new RoundResponse(roundJson.getDouble("delta-player-health"),
            roundJson.getDouble("delta-enemy-health"));
        return rsp;
    }

    private static ItemResponse loadItemResponse(JSONObject itemJson) {
        ItemResponse rsp = new ItemResponse(itemJson.getString("id"),
            itemJson.getString("type"));
        return rsp;
    }

    private static BattleResponse loadBattleResponse(JSONObject rsp) {
        List<RoundResponse> roundResponse = new ArrayList<>();
        JSONArray rrJson = rsp.getJSONArray("rounds");
        for (int i = 0; i < rrJson.length(); i++) {
            roundResponse.add(loadRoundResponse(rrJson.getJSONObject(i)));
        }

        List<ItemResponse> itemResponse = new ArrayList<>();
        JSONArray isJson = rsp.getJSONArray("battle-items");
        for (int i = 0; i < isJson.length(); i++) {
            itemResponse.add(loadItemResponse(isJson.getJSONObject(i)));
        }


        BattleResponse br = new BattleResponse(
            rsp.getString("enemy"),
            roundResponse,
            itemResponse,
            rsp.getDouble("initial-player-health"),
            rsp.getDouble("initial-enemy-health"));

        return br;
    }

    private static BattleFacade loadBattleFacade(JSONArray brsJson) {
        BattleFacade bf = new BattleFacade();
        List<BattleResponse> brs = new ArrayList<>();
        for (int i = 0; i < brsJson.length(); i++) {
            JSONObject brJson = brsJson.getJSONObject(i);
            BattleResponse br = loadBattleResponse(brJson);
            brs.add(br);
        }

        bf.setBattleResponses(brs);
        return bf;
    }

    /**
     * Turn the Game object to JSONObject
     * @return
     */
    private JSONObject toJson() {
        JSONArray entities = new JSONArray();
        for (Entity e : game.getMap().getEntities()) {
            entities.put(e.toJson());
        }
        JSONObject goalCondition = createJsonGoal(game.getGoals());

        JSONObject dungeon = new JSONObject();
        dungeon.put("entities", entities);
        dungeon.put("goal-condition", goalCondition);

        return dungeon;
    }

    /**
     * Get all saves in the saves Directory
     * @return
     */
    public static List<String> getAllSaves() {
        List<String> files = FileLoader.listFileNamesInResourceDirectory("saves");

        return files.stream().map(s -> s.replaceFirst("/.*", "")).collect(Collectors.toList());
    }

    /**
     * Load saveRecords from config file
     * @param config
     * @return
     */
    public static JSONArray loadSaveRecord() {
        String file = loadJsonFile(SAVE_CONFIG);

        if (file != null) {
            JSONObject saves = new JSONObject(file);

            return saves.getJSONArray("saves");
        }

        return null;
    }

    /**
     * Load given dungeon's saveRecord from config file
     * @param dungeon
     * @return
     */
    public static JSONObject loadSaveRecord(String dungeon) {
        JSONArray dungeons = loadSaveRecord();
        for (int i = 0; i < dungeons.length(); i++) {
            JSONObject d = (JSONObject) dungeons.get(i);
            if (d.getString("save-name").equals(dungeon)) return d;
        }

        return null;
    }

    public static JSONObject loadSaveDungeon(String dungeonName) {
        dungeonName = String.format("/saves/%s/%s.json", dungeonName, dungeonName);
        return new JSONObject(loadJsonFile(dungeonName));
    }

    public static JSONObject loadSaveConfig(String configName) {
        String path = String.format("/configs/%s.json", configName);
        return new JSONObject(loadJsonFile(path));
    }

    public static String getDungeonConfig(String dungeon) {
        JSONObject record = loadSaveRecord(dungeon);
        return record.getString("config");
    }

    /**
     *  Save the game with the given name
     *  Turns the game to a JSON file
     *  Write the json file to the save directory
     * @param game
     * @param name
     * @return
     * @throws IllegalArgumentException
     */
    public boolean saveGame(String name) throws IllegalArgumentException {
        if (gameJson == null || game == null) return false;
        String dungeon = NameConverter.getDungeonName(name);
        String saveName = NameConverter.getSaveName(name);
        String path = SAVE_PATH + "/" + dungeon;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        System.out.println("Saving: " + dungeon + "| dungeon json: " + saveName + " dungeon config: " + game.getConfig());
        if (addToSaveConfig(createRecord(dungeon, saveName, game.getConfig()))) {
            String jsonPath = dungeon + "/" + dungeon + ".json";
            // Save Inventory
            gameJson.put("inventory", inventoryJson(game.getPlayer().getInventory()));
            // Save InEffective
            Potion inEffective = game.getPlayer().getEffectivePotion();
            if (inEffective != null) {
                gameJson.put("ineffective", inEffective.toJson());
            }
            // Save PotionQueue
            gameJson.put("potion-queue", potionQueueJson(game.getPlayer().getPotionQueue()));

            // Save TriggerNext
            gameJson.put("next-trigger", game.getPlayer().getNextTrigger());

            // Save progressed goals
            gameJson.put("kills", game.getKills());
            gameJson.put("valuable-collect", game.getValuableCollect());
            
            // Save battle facade
            List<BattleResponse> rsp = game.getBattleFacade().getBattleResponses();
            JSONArray battleRsp = battleRspJson(rsp);
            gameJson.put("battle-facade", battleRsp);

            if (!writeJsonFile(jsonPath, gameJson)) {
                throw new IllegalArgumentException("Can't save game to path: " + SAVE_PATH + path);
            }
        }

        return true;
    }

    public static Game loadGame(String saveName) {
        System.out.println("Loading save: " + saveName);
        JSONObject dungeonJson = loadSaveDungeon(saveName);

        String config = getDungeonConfig(saveName);
        System.out.println("Loading config: " + config);

        JSONObject configJson = loadSaveConfig(config);
        GameBuilder builder = new GameBuilder();

        builder.setConfig(configJson);
        builder.setDungeon(dungeonJson);
        builder.setDungeonName(saveName);
        Game loadedGame = builder.buildGame();

        EntityFactory factory = new EntityFactory(configJson);
        // Load Tick
        loadedGame.setTick(dungeonJson.getInt("tick"));

        //Load Inventory
        JSONArray inv = dungeonJson.getJSONArray("inventory");
        loadedGame.getPlayer().getInventory().setItems((loadInventoryJson(factory, inv)));

        //Load PotionQueue
        JSONArray potionQueue = dungeonJson.getJSONArray("potion-queue");
        loadedGame.getPlayer().setPotionQueue(loadPotionQueue(factory, potionQueue));
        loadedGame.setConfigName(config);

        //Load ineffective
        if (dungeonJson.has("ineffective")) {
            JSONObject inEffectiveJson = dungeonJson.getJSONObject("ineffective");
            Potion inEffective = (Potion) factory.createEntity(inEffectiveJson);
            loadedGame.getPlayer().setEffectivePotion(inEffective);
        }

        //Load TriggerNext
        loadedGame.getPlayer().setNextTrigger(dungeonJson.getInt("next-trigger"));

        //Load progressed goals
        loadedGame.setKills(dungeonJson.getInt("kills"));
        loadedGame.setValuableCollect(dungeonJson.getInt("valuable-collect"));

        //Load battle facade
        loadedGame.setBattleFacade(loadBattleFacade(dungeonJson.getJSONArray("battle-facade")));
        return loadedGame;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public JSONObject getGameJson() {
        return gameJson;
    }

    public void setGameJson(JSONObject gameJson) {
        this.gameJson = gameJson;
    }
}

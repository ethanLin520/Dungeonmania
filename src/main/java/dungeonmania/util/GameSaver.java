package dungeonmania.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;


import dungeonmania.Game;
import dungeonmania.GameBuilder;
import dungeonmania.entities.Entity;
import dungeonmania.goals.ComplexGoal;
import dungeonmania.goals.Goal;

public class GameSaver {
    private static final String SAVE_PATH = "src/main/resources/saves";
    private static final String SAVE_CONFIG = "/saves/config.json";

    private Game game = null;
    private JSONObject gameJson = null;

    public GameSaver(Game game) {
        this.game = game;
        this.gameJson = toJson();
    }

    public GameSaver(JSONObject game) {
        this.gameJson = game;
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
            // Save Goals and Entities

            if (!writeJsonFile(jsonPath, gameJson)) {
                throw new IllegalArgumentException("Can't save game to path: " + SAVE_PATH + path);
            }
        }

        return true;
    }

    public Game loadGame(String saveName) {
        if (gameJson == null) return null;
        System.out.println("Loading save: " + saveName);
    
        String config = getDungeonConfig(saveName);
        System.out.println("Loading config: " + config);

        JSONObject configJson = loadSaveConfig(config);
        GameBuilder builder = new GameBuilder();

        builder.setConfig(configJson);
        builder.setDungeon(gameJson);
        builder.setDungeonName(saveName);
        game = builder.buildGame();

        
        game.setConfigName(config);

        return game;
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

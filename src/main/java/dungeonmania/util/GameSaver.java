package dungeonmania.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;


import dungeonmania.Game;
import dungeonmania.goals.ComplexGoal;
import dungeonmania.goals.Goal;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ResponseBuilder;


public class GameSaver {
    private static final String SAVE_PATH = "src/main/resources/saves";
    public static final String SAVE_CONFIG = "/saves/config.json";

    /**
     * Return the file content.
     * @param file
     * @return the whole file being loaded.
     */
    private static String loadJsonFile(String file) {
        String load = null;

        try {
            load = FileLoader.loadResourceFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            load = null;
        }

        return load;
    }

    private static boolean saveJsonFile(String name, JSONObject json) {

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
                saveJsonFile(name, json);
            }

            return true;
        } catch (IOException e) {
            System.out.println("Failed to save JSON file");
            e.printStackTrace();
        }

        return false;
    }
    
    private static JSONObject createConfigJson(String saveName, String dungeon, String config) {
        JSONObject o = new JSONObject();

        o.put("save-name", saveName);
        o.put("dungeon", dungeon);
        o.put("config", config);

        return o;
    }

    private static JSONObject createEntityJson(EntityResponse e) {
        JSONObject entity = new JSONObject();
        entity.put("type", e.getType());
        entity.put("x", e.getPosition().getX());
        entity.put("y", e.getPosition().getY());
        if (e.getType().equals("door") || e.getType().equals("key")) {
            entity.put("key", e.getKey());
        }

        return entity;
    }

    private static JSONObject createGoalJson(Goal goal) {
        JSONObject json = new JSONObject();
        json.put("goal", goal.goalType());
        
        if (goal instanceof ComplexGoal) {
            JSONArray subgoals = new JSONArray();

            ComplexGoal cgoal = (ComplexGoal) goal;
            for (Goal subgoal : cgoal.getSubgoal()) {
                subgoals.put(createGoalJson(subgoal));
            }

            json.put("subgoals", subgoals);
        }

        return json;
    }

    private static boolean addToSaveConfig(JSONObject newConfig) {
        JSONArray saves = loadConfig();

        int oldConfig = -1;
        for (int i = 0; i < saves.length(); i++) {
            JSONObject save = saves.getJSONObject(i);
            if (save.getString("save-name").equals(newConfig.getString("save-name"))) {
                oldConfig = i;
            } 
        }

        if (oldConfig != -1) {
            saves.remove(oldConfig);
        }
        saves.put(newConfig);

        JSONObject config = new JSONObject();
        config.put("saves", saves);


        return saveJsonFile("config.json", config);
    }
    
    private static JSONObject gameToJson(Game game) {
        DungeonResponse rsp = ResponseBuilder.getDungeonResponse(game);

        JSONArray entities = new JSONArray();
        for (EntityResponse e : rsp.getEntities()) {
            entities.put(createEntityJson(e));
        }
        JSONObject goalCondition = createGoalJson(game.getGoals());

        JSONObject dungeon = new JSONObject();
        dungeon.put("entities", entities);
        dungeon.put("goal-condition", goalCondition);

        return dungeon;
    }
    

    /**
     * Load saves from config file
     * @param config
     * @return
     */
    public static JSONArray loadConfig() {
        String file = loadJsonFile(SAVE_CONFIG);
        
        if (file != null) {
            JSONObject saves = new JSONObject(file);

            return saves.getJSONArray("saves");
        }
        
        return null;
    }

    /**
     * Get the JSON object of the given dungeon name.
     * @param dungeon
     * @return
     */
    public static JSONObject getDungeonSaveConfig(String dungeon) {
        JSONArray dungeons = loadConfig();
        for (int i = 0; i < dungeons.length(); i++) {
            JSONObject d = (JSONObject) dungeons.get(i);
            if (d.getString("save-name").equals(dungeon)) {

                return d;
            }
        }

        return null;
    }

    /**
     * Get all saves in the saves Directory
     * @return
     */
    public static List<String> getAllSaves() {
        List<String> files = FileLoader.listFileNamesInResourceDirectory("saves");

        return files.stream().map(s -> s.replaceFirst("/.*", "")).collect(Collectors.toList());
    }

    public static void saveGame(Game game, String name) throws IllegalArgumentException {
        System.out.println("Now saving: " + getSaveName(name) + " dungeon json: " + name);

        String saveName = getSaveName(name);
        name = getDugeonJsonName(name);
        String path = SAVE_PATH + "/" + saveName;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        if (addToSaveConfig(createConfigJson(saveName, name, game.getConfig()))) {
            String jsonPath = saveName + "/" + saveName + ".json";
            // Save Goals and Entities
            if (!saveJsonFile(jsonPath, gameToJson(game))) {
                throw new IllegalArgumentException("Can't save game to path:" + SAVE_PATH + path);
            }
            // Save Inventory
            // Save Battle Facade
        }
    }

    public static String getSaveName(String dungeonName) {
        String names[] = dungeonName.split("-");

        return names[0];
    }    
    
    public static String getDugeonJsonName(String dungeonName) {
        String names[] = dungeonName.split("-");

        return names[0] + "-" + names[names.length - 1];
    }
}

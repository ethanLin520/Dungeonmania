package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.GameSaver;

public class DungeonManiaController {
    private Game game = null;
    public static final List<String> VALID_BUILDABLES = List.of("bow", "shield", "midnight_armour", "sceptre");

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    public static List<String> saves() {
        return GameSaver.getAllSaves();
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }

        if (!configs().contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            game = builder.setConfigName(configName).setDungeonName(dungeonName).buildGame();
            game.setConfig(configName);
            return ResponseBuilder.getDungeonResponse(game);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return ResponseBuilder.getDungeonResponse(game);
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.tick(itemUsedId));
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        return ResponseBuilder.getDungeonResponse(game.tick(movementDirection));
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        if (!VALID_BUILDABLES.contains(buildable)) {
            throw new IllegalArgumentException("Only bow, shield, midnight_armour and sceptre can be built");
        }

        return ResponseBuilder.getDungeonResponse(game.build(buildable));
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.interact(entityId));
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        GameSaver.saveGame(game, name);
        return ResponseBuilder.getDungeonResponse(game);
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        String saveName = GameSaver.getSaveName(name);
        System.out.println("Now loading save: " + saveName + " DungeonJson: " + name);

        JSONObject saves = GameSaver.getDungeonSaveConfig(saveName);

        String dungeonName = saves.getString("save-name");
        if (!saves().contains(dungeonName)) {
            throw new IllegalArgumentException(saveName + " is not a dungeon that exists");
        }

        String dungeonConfig = saves.getString("config");
        if (!configs().contains(dungeonConfig)) {
            throw new IllegalArgumentException(saveName + " has a configuration that does not exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            game = builder.setConfigName(dungeonConfig).setSaveDungeonName(dungeonName).buildGame();
            game.setConfig(dungeonConfig);
            return ResponseBuilder.getDungeonResponse(game);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        List<String> games = new ArrayList<>();
        JSONArray saves = GameSaver.loadConfig();
        for (int i = 0; i < saves.length(); i++) {
            JSONObject dungeon = (JSONObject) saves.get(i);
            games.add(dungeon.getString("dungeon"));
        }
        System.out.println("Found " + games.size() + " games on your local saves");
        return games;
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(
            int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        return null;
    }

    /**
     * /game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        if (ticks <= 0) {
            throw new IllegalArgumentException("Input <tick> must be greater than 0!");
        }
        return ResponseBuilder.getDungeonResponse(game);
    }

}

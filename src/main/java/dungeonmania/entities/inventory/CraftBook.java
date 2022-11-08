package dungeonmania.entities.inventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class CraftBook {
    String path = "saves";
    public CraftBook() {
        JSONArray json = parser();
    }


    private JSONArray parser() {
        String jsonText;
        JSONObject json = null;
        JSONArray jarray = null;

        try {
            jsonText = Files.readString(Path.of(path));
            json = new JSONObject(jsonText); 
        } catch (IOException e) {
            System.out.println("Can't read file, file path: " + path);
        }
        
        if (json != null) {
            jarray = (JSONArray) json.get("craftbook");
        }

        return jarray;
    }
}

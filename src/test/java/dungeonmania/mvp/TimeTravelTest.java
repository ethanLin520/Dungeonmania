package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TimeTravelTest {
    @Test
    @Tag("19-1")
    @DisplayName("Test basic time travel.")
    public void basicTimeTravel() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TTT_basic", "c_TTT");

        assertEquals(3, TestUtils.getEntities(res, "treasure").size());
        assertEquals(1, TestUtils.getEntities(res, "time_turner").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        res = assertDoesNotThrow(() -> dmc.rewind(5));

        // Entities were still there
        assertEquals(3, TestUtils.getEntities(res, "treasure").size());
        assertEquals(1, TestUtils.getEntities(res, "time_turner").size());

        // Inventory
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

        // Og player
        assertEquals(new Position(2, 1), TestUtils.getEntities(res, "older_player").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT);
        // Og player follows
        assertEquals(new Position(2, 1), TestUtils.getEntities(res, "older_player").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT);
        // Og player collect treasure
        assertEquals(2, TestUtils.getEntities(res, "treasure").size());

        res = dmc.tick(Direction.RIGHT);
        // Og player follows
        assertEquals(new Position(4, 1), TestUtils.getEntities(res, "older_player").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // Og player collect time turner
        assertEquals(0, TestUtils.getEntities(res, "time_turner").size());

        res = dmc.tick(Direction.RIGHT);
        // Og player gone
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
    }

    @Test
    @Tag("19-2")
    @DisplayName("Test battle og player.")
    public void basicTimeTravelBattle() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TTT_battle", "c_TTT");

        assertEquals(1, TestUtils.getEntities(res, "time_turner").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "time_turner").size());

        res = assertDoesNotThrow(() -> dmc.rewind(5));

        // Og player
        assertEquals(new Position(1, 1), TestUtils.getEntities(res, "older_player").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        // battle w/ og player
        assertNotEquals(0, res.getBattles().size());

        // Og player gone
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
    }
    @Test
    @Tag("19-3")
    @DisplayName("Test Time Travel Portal")
    public void basicTimeTravelPortal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TTT_portal", "c_TTT");

        assertEquals(1, TestUtils.getEntities(res, "time_travelling_portal").size());

        for (int i = 0; i < 30; i++) {
            res = dmc.tick(Direction.RIGHT);
        }

        // Og player
        assertEquals(new Position(1, 1), TestUtils.getEntities(res, "older_player").get(0).getPosition());

        for (int i = 0; i < 10; i++) {
            res = dmc.tick(Direction.DOWN);
        }

        // Og player
        assertEquals(new Position(11, 1), TestUtils.getEntities(res, "older_player").get(0).getPosition());

        for (int i = 0; i < 20; i++) {
            res = dmc.tick(Direction.DOWN);
        }

        // Og player gone
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
    }
}

package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SceptreTest {
    @Test
    @Tag("17-1")
    @DisplayName("Test building a Sceptre with 1 wood 1 key 1 sun stone")
    public void buildSceptreWith1W1K1SS() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptre_1W1K1SS", "c_BuildablesTest_BuildSceptre");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Pick up SS
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        // SS retains
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("17-2")
    @DisplayName("Test building a Sceptre with 2 arrow 1 key 1 sun stone")
    public void buildSceptreWith2A1K1SS() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptre_2A1K1SS", "c_BuildablesTest_BuildSceptre");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());

        // Pick up Key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Pick up SS
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        // SS retains
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("17-3")
    @DisplayName("Test building a Sceptre with 2 arrow 1 treasure 1 sun stone")
    public void buildSceptreWith2A1T1SS() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptre_2A1T1SS", "c_BuildablesTest_BuildSceptre");

        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());

        // Pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Pick up SS
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        // SS retains
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("17-4")
    @DisplayName("Test building a Sceptre with 2 arrow 2 sun stone")
    public void buildSceptreWith2A2SS() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptre_2A2SS", "c_BuildablesTest_BuildSceptre");

        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());

        // Pick up SS
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        // SS retains
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("17-5")
    @DisplayName("Test Sceptre can do mind control and mind control effect disappears")
    public void sceptreMindControl() {
        /*
         * P (1,1)                                            M (15,1)
         * P -> 4 step + 1 build + 1 interact       6 step <- M
         * P (5,1)                                            M (9,1)
         *
         * P -> 2 step -> (7,1)                     1 step & move to P'prev pos @ (6,1)
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_MindControlTest", "c_MindControlTest");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());

        // Pick up SS
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        // SS retains
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Mind Control
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // -> 2 step
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // No Battle
        assertEquals(0, res.getBattles().size());

        // -> 4 step
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // No Battle
        assertEquals(0, res.getBattles().size());

        // Go back and battle
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // Battle
        assertNotEquals(0, res.getBattles().size());
    }
}

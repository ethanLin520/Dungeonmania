package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SwampTest {
    @Test
    @Tag("18-1")
    @DisplayName("Test swamp can stuck an enemy.")
    public void swampStuck() {
        /*
         *  Move Factor = 3
         * P (1,1)                      S (5,1)              M (6,1)
         * P -> 3 step -> (4,1)         S (5,1) <- 1 step <- M
         * no battle
         */
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTestMovement", "c_SwampTestMovement");

        assertEquals(new Position(6, 1), MercenaryTest.getMercPos(res));

        res = dmc.tick(Direction.RIGHT);    // M (5,1) stuck
        assertEquals(new Position(5, 1), MercenaryTest.getMercPos(res));
        res = dmc.tick(Direction.RIGHT);    // stuck
        res = dmc.tick(Direction.RIGHT);    // stuck

        assertEquals(new Position(5, 1), MercenaryTest.getMercPos(res));
        assertEquals(0, res.getBattles().size());
    }

}

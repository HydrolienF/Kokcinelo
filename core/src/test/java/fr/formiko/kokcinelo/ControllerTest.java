package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.Level;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ControllerTest extends Assertions {
    private float epsilon = 0.01f;

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        "200, 0, 400, 0, 500, 0.5",
        "200, 0, 400, 0, 800, 0",
        "200, 0, 400, 0, 400, 1",
        "1000, 0, 400, 50, 450, 0.92",
        "100, 200, 400, 285, 475, 0",
    })
    // @formatter:on
    void testGetSoundVolume(float hearRadius, float targetX, float targetY, float sourceX, float sourceY, float expectedVolume) {
        Creature target = new Ant();
        target.setHearRadius(hearRadius);
        target.setCenter(targetX, targetY);
        Creature source = new Ant();
        source.setCenter(sourceX, sourceY);
        assertEquals(expectedVolume, Controller.getSoundVolume(source, target));
    }

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        "200, 0, 0, 0, 0, 0",
        "200, 0, -240, 0, 0, 0",
        "200, 0, 0, 10, 0, 1",
        "200, 0, 0, -3205010, 0, -1",
        "200, 0, 0, 100, 100, 0.5",
        "200, 0, 0, 100, -100, 0.5",
        "200, 0, 0, -100, 100, -0.5",
        "200, 0, 0, -100, -100, -0.5",
        "200, 0, 0, 200, 100, 0.6666",
        "200, 0, 0, 1000, 100, 0.91",
    })
    // @formatter:on
    void testGetSoundPan(float hearRadius, float targetX, float targetY, float sourceX, float sourceY, float expectedPan) {
        Creature target = new Ant();
        target.setHearRadius(hearRadius);
        target.setCenter(targetX, targetY);
        Creature source = new Ant();
        source.setCenter(sourceX, sourceY);
        assertEquals(expectedPan, Controller.getSoundPan(source, target));
    }

    @Test
    void testLoadUnlockedLevels() {
        Controller.loadUnlockedLevels("");
        onlyThisLevelAreUnlocked("1K");
    }
    @Test
    void testLoadUnlockedLevels2() {
        Controller.loadUnlockedLevels(null);
        onlyThisLevelAreUnlocked("1K");
    }
    @Test
    void testLoadUnlockedLevels3() {
        Controller.loadUnlockedLevels("1K,0,1234567");
        onlyThisLevelAreUnlocked("1K");
    }
    @Test
    void testLoadUnlockedLevels4() {
        Controller.loadUnlockedLevels("1K,49,1234567");
        onlyThisLevelAreUnlocked("1K");
    }
    @Test
    void testLoadUnlockedLevels5() {
        Controller.loadUnlockedLevels("1K,50,1234567");
        onlyThisLevelAreUnlocked("1K", "2K", "2F");
    }
    @Test
    void testLoadUnlockedLevels6() {
        Controller.loadUnlockedLevels("1K,1,1234567\n2K,1,1234567");
        onlyThisLevelAreUnlocked("1K", "2K");
    }
    @Test
    void testLoadUnlockedLevels7() {
        Controller.loadUnlockedLevels("2K,1,1234567");
        onlyThisLevelAreUnlocked("1K", "2K");
    }
    @Test
    void testLoadUnlockedLevels8() {
        Controller.loadUnlockedLevels("2K,1,1234567\n\n\n\n\n");
        onlyThisLevelAreUnlocked("1K", "2K");
    }
    @Test
    void testLoadUnlockedLevels9() {
        Controller.loadUnlockedLevels("4K,1,1234567");
        onlyThisLevelAreUnlocked("1K", "4K");
    }
    @Test
    void testLoadUnlockedLevels10() {
        Controller.loadUnlockedLevels("4K,51,1234567");
        onlyThisLevelAreUnlocked("1K", "4K", "5K");
    }
    @Test
    void testLoadUnlockedLevels11() {
        Controller.loadUnlockedLevels("4K,51,1234567\n1K,51,1234567");
        onlyThisLevelAreUnlocked("1K", "4K", "5K", "2K", "2F");
    }

    void onlyThisLevelAreUnlocked(String... levelIds) {
        Set<String> levelIdsSet = Set.of((levelIds));
        for (Level level : Level.getLevelList()) {
            if (levelIdsSet.contains(level.getId())) {
                assertTrue(level.isUnlocked(), "levelId: " + level.getId());
            } else {
                assertFalse(level.isUnlocked(), "levelId: " + level.getId());
            }
        }
    }


    boolean almostEquals(float a, float b) { return Math.abs(a - b) < epsilon; }
}

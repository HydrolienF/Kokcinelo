package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.Level;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

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
        assertTrue(almostEquals(expectedVolume, Controller.getSoundVolume(source, target)));
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
        assertTrue(almostEquals(expectedPan, Controller.getSoundPan(source, target)));
    }

    @ParameterizedTest
    @MethodSource("unlockedLevels")
    void testLoadUnlockedLevels(String contentOfTheFile, Set<String> expectedUnlockedLevels) {
        Controller.loadUnlockedLevels(contentOfTheFile);
        onlyThisLevelAreUnlocked(expectedUnlockedLevels);
    }

    private static Stream<Arguments> unlockedLevels() {
        //@formatter:off
        return Stream.of(
            Arguments.of("", Set.of("1K")),
            Arguments.of(null, Set.of("1K")),
            Arguments.of("1K,0,1234567", Set.of("1K")),
            Arguments.of("1K,49,1234567", Set.of("1K")),
            Arguments.of("1K,50,1234567", Set.of("1K", "2K", "2F")),
            Arguments.of("1K,1,1234567\n2K,1,1234567", Set.of("1K", "2K")),
            Arguments.of("2K,1,1234567", Set.of("1K", "2K")),
            Arguments.of("2K,1,1234567\n\n\n\n\n", Set.of("1K", "2K")),
            Arguments.of("4K,1,1234567", Set.of("1K", "4K")),
            Arguments.of("4K,51,1234567", Set.of("1K", "4K", "5K")),
            Arguments.of("4K,51,1234567\n1K,51,1234567", Set.of("1K", "4K", "5K", "2K", "2F"))
        );
        //@formatter:on
    }

    void onlyThisLevelAreUnlocked(Set<String> levelIdsSet) {
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

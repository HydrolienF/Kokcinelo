package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.Controller;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import com.badlogic.gdx.math.Vector2;

class CreatureTest extends Assertions {
    @Test
    void testCreature() {
        Creature c = new CreatureX();
        assertNotNull(c);
    }

    @Test
    void testSee() {
        Creature c = new CreatureX(10, 20);
        c.setCenter(0, 0);
        Creature c2 = new CreatureX(10, 20);
        c2.setCenter(20, 20);
        Creature c3 = new CreatureX(10, 50);
        c3.setCenter(40, 40);

        assertTrue(c.see(c2));
        assertTrue(c2.see(c));
        assertTrue(c3.see(c2));
        assertTrue(c2.see(c3));
        assertTrue(c3.see(c2));
        assertTrue(c2.see(c3));

        assertTrue(c3.see(c));
        assertFalse(c.see(c3));
    }
    @Test
    void testCreatureMove() {
        Creature c = new CreatureX();
        c.setCenter(0, 0);
        c.setRotation(0);
        c.getActor().moveFront(1f);
        almostEquals(0, c.getCenterX());
        almostEquals(1, c.getCenterY());
    }

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        "1, 0, 0, 0, 1, 0, 1",
        "1, 0, 0, 0, 0.2f, 0, 0.2f",
        "4.5f, 0, 0, 0, 0.2f, 0, 0.9f",
        "4.5f, 0, 0, 90, 0.2f, -0.9f, 0",
        "5f, 0, 0, 45, 0.2f, -0.70711f, 0.70711f",
    })
    // @formatter:on
    void testCreatureMoveWithSpeed(float movingSpeed, float x, float y, float rotation, float distance, float expectedX, float expectedY) {
        Creature c = new CreatureX();
        c.setMovingSpeed(movingSpeed);
        c.setCenter(x, y);
        c.setRotation(rotation);
        c.moveFront(distance);
        almostEquals(expectedX, c.getCenterX());
        almostEquals(expectedY, c.getCenterY());
    }

    @Test
    void testGoTo() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenter(0, 0);
        c.setRotation(0);
        c.goTo(new Vector2(1, 1));
        assertEquals(45f, c.getWantedRotation());
    }
    @Test
    void testGoTo2() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenter(0, 0);
        c.setRotation(0);
        c.goTo(new Vector2(-2, 1));
        almostEquals(296.56506f, c.getWantedRotation());
    }

    @Test
    void testRunAwayFrom() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenter(0, 0);
        c.setRotation(0);
        c.runAwayFrom(new LinkedList<Float>(), new Vector2(1, 1));
        almostEquals(225f, c.getWantedRotation());
    }

    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> new Ant());
        assertDoesNotThrow(() -> new RedAnt());
        assertDoesNotThrow(() -> new GreenAnt());
        assertDoesNotThrow(() -> new Ladybug());
        assertDoesNotThrow(() -> new LadybugSideView());
        assertDoesNotThrow(() -> new Aphid());
    }

    void createGameStateWithAphidLadybugAnt(int aphid, int ladybug, int ant, String levelId) {
        GameState gs = GameState.builder().setMapHeight(2000).setMapWidth(2000)
                .setLevel(Level.newTestLevel(levelId, Map.of(Aphid.class, aphid, Ladybug.class, ladybug, RedAnt.class, ant), false))
                .build();
        Controller.setController(new Controller(null));
        Controller.getController().setGameState(gs);
        Controller.setDebug(false);
    }
    void createGameStateWithAphidLadybugAnt(int aphid, int ladybug, int ant) {
        createGameStateWithAphidLadybugAnt(aphid, ladybug, ant, "1K");
    }

    @Test
    void testGetVisibleCreatureHuntedBy() {
        createGameStateWithAphidLadybugAnt(0, 1, 1);
        Ant a = Controller.getController().getGameState().getAnts().get(0);
        Ladybug l = Controller.getController().getGameState().getLadybugs().get(0);

        a.setCenter(0, 0);
        l.setCenter(1, 0);

        assertFalse(a.getVisibleCreatureHuntedBy().contains(l));
        assertTrue(l.getVisibleCreatureHuntedBy().contains(a));
    }

    @Test
    void testGetVisibleCreatureHuntedBy2() {
        createGameStateWithAphidLadybugAnt(0, 1, 1);
        Ant a = Controller.getController().getGameState().getAnts().get(0);
        Ladybug l = Controller.getController().getGameState().getLadybugs().get(0);

        a.setCenter(0, 0);
        l.setCenter(1500, 0);

        assertFalse(a.getVisibleCreatureHuntedBy().contains(l));
        // assertFalse(l.getVisibleCreatureHuntedBy().contains(a));
    }

    @Test
    void testGetVisibleCreatureHuntedBy3() {
        createGameStateWithAphidLadybugAnt(0, 2, 1);
        Ant a = Controller.getController().getGameState().getAnts().get(0);
        Ladybug l = Controller.getController().getGameState().getLadybugs().get(0);
        Ladybug l2 = Controller.getController().getGameState().getLadybugs().get(1);

        a.setCenter(0, 0);
        l.setCenter(1, 0);
        l2.setCenter(1, 0);

        assertFalse(a.getVisibleCreatureHuntedBy().contains(l));
        assertTrue(l.getVisibleCreatureHuntedBy().contains(a));
        assertTrue(l2.getVisibleCreatureHuntedBy().contains(a));
    }

    @Test
    void testGetVisibleCreatureHuntedByAphid() {
        createGameStateWithAphidLadybugAnt(2, 2, 0);
        Ladybug l = Controller.getController().getGameState().getLadybugs().get(0);
        Ladybug l2 = Controller.getController().getGameState().getLadybugs().get(1);
        Aphid a = Controller.getController().getGameState().getAphids().get(0);
        Aphid a2 = Controller.getController().getGameState().getAphids().get(1);

        a.setCenter(0, 0);
        a2.setCenter(50, 0);
        l.setCenter(100, 0);
        l2.setCenter(150, 0);

        assertTrue(l.getVisibleCreatureHuntedBy().isEmpty());
        assertTrue(a.getVisibleCreatureHuntedBy().contains(l));
        assertTrue(a2.getVisibleCreatureHuntedBy().contains(l));
        assertTrue(a.getVisibleCreatureHuntedBy().contains(l2)); // because of Ladybug size
        assertTrue(a2.getVisibleCreatureHuntedBy().contains(l2));
    }
    @Test
    void testGetVisibleCreatureHuntedByAphid2() {
        createGameStateWithAphidLadybugAnt(2, 2, 0);
        Ladybug l = Controller.getController().getGameState().getLadybugs().get(0);
        Ladybug l2 = Controller.getController().getGameState().getLadybugs().get(1);
        Aphid a = Controller.getController().getGameState().getAphids().get(0);
        Aphid a2 = Controller.getController().getGameState().getAphids().get(1);

        a.setCenter(0, 0);
        a2.setCenter(50, 0);
        l.setCenter(100, 0);
        l2.setCenter(150 + l2.getHitRadius(), 0);

        assertTrue(l.getVisibleCreatureHuntedBy().isEmpty());
        assertTrue(a.getVisibleCreatureHuntedBy().contains(l));
        assertTrue(a2.getVisibleCreatureHuntedBy().contains(l));
        assertFalse(a.getVisibleCreatureHuntedBy().contains(l2)); // because of Ladybug size
        assertTrue(a2.getVisibleCreatureHuntedBy().contains(l2));
    }
    @Test
    void testGetClosestVisibleCreatureToHunt() {
        createGameStateWithAphidLadybugAnt(2, 3, 1);
        Ladybug l = Controller.getController().getGameState().getLadybugs().get(0);
        Ladybug l2 = Controller.getController().getGameState().getLadybugs().get(1);
        Ladybug l3 = Controller.getController().getGameState().getLadybugs().get(2);
        Aphid a = Controller.getController().getGameState().getAphids().get(0);
        Aphid a2 = Controller.getController().getGameState().getAphids().get(1);

        a.setCenter(0, 0);
        a2.setCenter(50, 0);
        l.setCenter(100, 0);
        l2.setCenter(150, 0);
        l3.setCenter(1500, 0);

        assertEquals(a2, l.getClosestVisibleCreatureToHunt());
        assertEquals(a2, l2.getClosestVisibleCreatureToHunt());
        assertNull(l3.getClosestVisibleCreatureToHunt());
        assertNull(a.getClosestVisibleCreatureToHunt());
    }

    // Test for runAwayFrom are mostly graphics test


    @Test
    void testRunAwayFromSeveralEnemies() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenter(0, 0);
        c.setRotation(0);
        c.runAwayFrom(new LinkedList<Float>(), new Vector2(1, 1), new Vector2(-1, 1));
        almostEquals(180, c.getWantedRotation());
    }

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        // 0 wall
        "100, 100, 20, -1, -1",
        // 1 wall
        "5, 100, 20, 180f, -1",
        "15, 100, 40, 180f, -1",
        "19, 100, 40, 180f, -1",
        "1, 100, 20, 180f, -1",
        "0, 100, 20, 180f, -1",
        "1999, 100, 20, 0f, -1",
        "2000, 100, 20, 0f, -1",
        "100, 5, 20, 270, -1",
        "100, 15, 40, 270, -1",
        "100, 19, 40, 270, -1",
        // 2 walls
        "5, 5, 20, 180, 270", // with common part
        "19, 19, 40, 180, 270", // without common part
        "1994, 1992, 20, 0f, 90f",
    })
    // @formatter:on
    void testGetWallsAngles(float x, float y, int visionRadius, float angle1, float angle2) {
        createGameStateWithAphidLadybugAnt(1, 1, 0);
        Creature c = new CreatureX(10, visionRadius);
        c.setCenter(x, y);
        List<Float> wallsAngles = c.getWallsAngles();
        int angleNumber = 0;
        if (angle1 != -1) {
            assertEquals(angle1, wallsAngles.get(0));
            angleNumber++;
        }
        if (angle2 != -1) {
            assertEquals(angle2, wallsAngles.get(1));
            angleNumber++;
        }
        assertEquals(angleNumber, wallsAngles.size());
    }
    @ParameterizedTest
    @MethodSource("creatureProvider")
    void testToString(Creature c, Set<String> contains) {
        String s = c.toString();
        for (String str : contains) {
            assertTrue(s.contains(str));
        }
    }

    private static Stream<Arguments> creatureProvider() {
        Creature ladybug = new Ladybug();
        ladybug.setCenter(-7.356f, 8.2425f);
        ladybug.setRotation(0.123456f);
        ladybug.setMovingSpeed(0.2f);
        return Stream.of(Arguments.of(new Ant(), Set.of("Ant")),
                Arguments.of(ladybug, Set.of("Ladybug", "8.2425", "-7.356", "0.123456", "0.2")));
    }

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
        "1, 1, 0, 0K, K, 1, 0, 1",
        "1, 1, 0, 0K, A, 1, 1, 0",
        "1, 1, 0, 0K, A, 1, 0, 0", // can't go under 0
        "1, 1, 0, 0K, A, -1, 0, 1",
        "1, 1, 0, 0K, K, 17, 0, 17",
        "1, 1, 1, 0K, K, 1, 0, 1",
        "100, 3, 4, 0K, K, 1, 0, 1",
        "1, 1, 1, 0K, F, 1, 2, 1",

        "1, 1, 1, 0F, K, 1, 100, 99",
        "1, 1, 1, 0F, F, 1, 50, 51",
        "1, 1, 1, 1A, A, 1, 50, 51",
        "1, 1, 1, 0A, K, 1, 99, 98",
        "1, 1, 1, 0A, F, 1, 99, 100",
        "1, 1, 1, 0F, A, 1, 99, 100",
        "1, 1, 1, 0F, A, 1, 100, 100", // not more than max score
    })
    // @formatter:on
    void testAddScore(int nbAphids, int nbLadybugs, int nbAnts, String levelId, String creatureToAddScore, int scoreToAdd, int defaultScore,
            int expectedScore) {
        createGameStateWithAphidLadybugAnt(nbAphids, nbLadybugs, nbAnts, levelId);
        Creature c;
        switch (creatureToAddScore) {
            case "A":
                c = Controller.getController().getGameState().getAphids().get(0);
                break;
            case "K":
                c = Controller.getController().getGameState().getLadybugs().get(0);
                break;
            case "F":
                c = Controller.getController().getGameState().getAnts().get(0);
                break;
            default:
                c = null;
                assertTrue(false);
        }
        Controller.getController().getGameState().setMaxScore(100);
        Controller.getController().getLocalPlayer().setScore(defaultScore);
        c.addScore(scoreToAdd);
        assertEquals(expectedScore, Controller.getController().getLocalPlayer().getScore());
    }


    class CreatureX extends Creature {
        CreatureX(int hitRadius, int visionRadius) {
            super("x");
            this.hitRadius = hitRadius;
            this.visionRadius = visionRadius;
        }
        CreatureX() { this(10, 20); }
    }
    void almostEquals(float f1, float f2) { assertTrue(java.lang.Math.abs(f1 - f2) < 0.1f, f1 + " ≃ " + f2); }
    void almostEqualsAngle(float f1, float f2) {
        if (java.lang.Math.abs(f1 - f2) > 180) {
            if (f1 > f2) {
                f1 -= 360;
            } else {
                f2 -= 360;
            }
        }
        almostEquals(f1, f2);
    }
}

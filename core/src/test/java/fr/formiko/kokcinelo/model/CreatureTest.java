package fr.formiko.kokcinelo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Vector2;

public class CreatureTest extends Assertions {
    @Test
    void testCreature() {
        Creature c = new CreatureX();
        assertNotNull(c);
    }
    @Test
    void testCreatureMove() {
        Creature c = new CreatureX();
        // c.setMovingSpeed(1f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(0);
        c.getActor().moveFront(1f);
        almostEquals(0, c.getCenterX());
        almostEquals(1, c.getCenterY());
    }
    @Test
    void testCreatureMoveWithSpeed() {
        Creature c = new CreatureX();
        c.setMovingSpeed(1f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(0);
        c.moveFront(1f);
        almostEquals(0, c.getCenterX());
        almostEquals(1, c.getCenterY());
    }
    @Test
    void testCreatureMoveWithSpeed2() {
        Creature c = new CreatureX();
        c.setMovingSpeed(1f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(0);
        c.moveFront(0.2f);
        almostEquals(0, c.getCenterX());
        almostEquals(0.2f, c.getCenterY());
    }
    @Test
    void testCreatureMoveWithSpeed3() {
        Creature c = new CreatureX();
        c.setMovingSpeed(4.5f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(0);
        c.moveFront(0.2f);
        almostEquals(0, c.getCenterX());
        almostEquals(0.9f, c.getCenterY());
    }
    @Test
    void testCreatureMoveWithSpeedY() {
        Creature c = new CreatureX();
        c.setMovingSpeed(4.5f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(90);
        c.moveFront(0.2f);
        almostEquals(-0.9f, c.getCenterX());
        almostEquals(0, c.getCenterY());
    }
    @Test
    void testCreatureMoveWithSpeedXY() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(45);
        c.moveFront(0.2f);
        almostEquals(-0.70711f, c.getCenterX());
        almostEquals(0.70711f, c.getCenterY());
    }

    @Test
    void testGoTo() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(0);
        c.goTo(new Vector2(1, 1));
        assertEquals(45f, c.getWantedRotation());
    }
    @Test
    void testGoTo2() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(0);
        c.goTo(new Vector2(-2, 1));
        almostEquals(296.56506f, c.getWantedRotation());
    }

    @Test
    void testRunAwayFrom() {
        Creature c = new CreatureX();
        c.setMovingSpeed(5f);
        c.setCenterX(0);
        c.setCenterY(0);
        c.setRotation(0);
        c.runAwayFrom(new Vector2(1, 1));
        almostEquals(225f, c.getWantedRotation());
    }

    // @Test
    // void testRunAwayFromSeveralEnemies() {
    // Creature c = new CreatureX();
    // c.setMovingSpeed(5f);
    // c.setCenterX(0);
    // c.setCenterY(0);
    // c.setRotation(0);
    // c.runAwayFrom(new Vector2(1, 1), new Vector2(-1, 1));
    // almostEquals(1800, c.getWantedRotation()); // isn't it a bug ? It should be 180° not 0°
    // }


    class CreatureX extends Creature {
        public CreatureX() { super("x"); }
        @Override
        public void moveAI(GameState gs) {}
    }

    void almostEquals(float f1, float f2) { assertTrue(Math.abs(f1 - f2) < 0.001f, f1 + " ≃ " + f2); }
}

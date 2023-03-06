package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Creature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ControllerTest extends Assertions {
    private float epsilon = 0.01f;
    @Test
    public void testGetSoundVolume() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(400);
        Creature source = new Ant();
        source.setCenterX(0);
        source.setCenterY(500);
        assertEquals(0.5f, Controller.getSoundVolume(source, target));
    }
    @Test
    public void testGetSoundVolume2() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(400);
        Creature source = new Ant();
        source.setCenterX(0);
        source.setCenterY(800);
        assertEquals(0f, Controller.getSoundVolume(source, target));
    }
    @Test
    public void testGetSoundVolume3() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(400);
        Creature source = new Ant();
        source.setCenterX(0);
        source.setCenterY(400);
        assertEquals(1f, Controller.getSoundVolume(source, target));
    }
    @Test
    public void testGetSoundVolume4() {
        Creature target = new Ant();
        target.setHearRadius(1000);
        target.setCenterX(0);
        target.setCenterY(400);
        Creature source = new Ant();
        source.setCenterX(50);
        source.setCenterY(450);
        assertTrue(almostEquals(0.92f, Controller.getSoundVolume(source, target)));
    }
    @Test
    public void testGetSoundVolume5() {
        Creature target = new Ant();
        target.setHearRadius(100);
        target.setCenterX(200);
        target.setCenterY(400);
        Creature source = new Ant();
        source.setCenterX(285);
        source.setCenterY(475);
        assertTrue(almostEquals(0f, Controller.getSoundVolume(source, target)));
    }

    @Test
    public void testGetSoundPan() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(0);
        source.setCenterY(0);
        assertEquals(0f, Controller.getSoundPan(source, target));
    }
    @Test
    public void testGetSoundPan2() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(-240);
        Creature source = new Ant();
        source.setCenterX(0);
        source.setCenterY(0);
        assertTrue(almostEquals(0f, Controller.getSoundPan(source, target)));
    }
    @Test
    public void testGetSoundPan3() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(10);
        source.setCenterY(0);
        assertEquals(1f, Controller.getSoundPan(source, target));
    }
    @Test
    public void testGetSoundPan4() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(-3205010);
        source.setCenterY(0);
        assertEquals(-1f, Controller.getSoundPan(source, target));
    }
    @Test
    public void testGetSoundPan5() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(100);
        source.setCenterY(100);
        assertEquals(0.5f, Controller.getSoundPan(source, target));
    }
    @Test
    public void testGetSoundPan6() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(100);
        source.setCenterY(-100);
        assertEquals(0.5f, Controller.getSoundPan(source, target));
    }
    @Test
    public void testGetSoundPan7() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(-100);
        source.setCenterY(100);
        assertEquals(-0.5f, Controller.getSoundPan(source, target));
    }
    @Test
    public void testGetSoundPan8() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(-100);
        source.setCenterY(-100);
        assertEquals(-0.5f, Controller.getSoundPan(source, target));
    }

    @Test
    public void testGetSoundPan9() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(200);
        source.setCenterY(100);
        assertTrue(almostEquals(0.6666f, Controller.getSoundPan(source, target)));
    }
    @Test
    public void testGetSoundPan10() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(1000);
        source.setCenterY(100);
        assertTrue(almostEquals(0.91f, Controller.getSoundPan(source, target)));
    }


    public boolean almostEquals(float a, float b) { return Math.abs(a - b) < epsilon; }
}

package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.Level;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ControllerTest extends Assertions {
    private float epsilon = 0.01f;
    @Test
    void testGetSoundVolume() {
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
    void testGetSoundVolume2() {
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
    void testGetSoundVolume3() {
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
    void testGetSoundVolume4() {
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
    void testGetSoundVolume5() {
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
    void testGetSoundPan() {
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
    void testGetSoundPan2() {
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
    void testGetSoundPan3() {
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
    void testGetSoundPan4() {
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
    void testGetSoundPan5() {
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
    void testGetSoundPan6() {
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
    void testGetSoundPan7() {
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
    void testGetSoundPan8() {
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
    void testGetSoundPan9() {
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
    void testGetSoundPan10() {
        Creature target = new Ant();
        target.setHearRadius(200);
        target.setCenterX(0);
        target.setCenterY(0);
        Creature source = new Ant();
        source.setCenterX(1000);
        source.setCenterY(100);
        assertTrue(almostEquals(0.91f, Controller.getSoundPan(source, target)));
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

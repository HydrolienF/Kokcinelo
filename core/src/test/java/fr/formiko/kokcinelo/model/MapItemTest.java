package fr.formiko.kokcinelo.model;

import org.junit.jupiter.api.Assertions;

public class MapItemTest extends Assertions {

    // @Test
    // public void testMapItem() {
    // // assertEquals("FILE:" + new File(".").getAbsolutePath(), "");
    // MapItem mi1 = new MapItemX();
    // mi1.getActor().setCenterX(0);
    // mi1.getActor().setCenterY(0);
    // assertNotNull(mi1.getActor());
    // }
    // @Test
    // public void testIsInRadius() {
    // MapItem mi1 = new MapItemX();
    // MapItem mi2 = new MapItemX();
    // mi1.getActor().setCenterX(0);
    // mi1.getActor().setCenterY(0);
    // mi2.getActor().setCenterX(2);
    // mi2.getActor().setCenterY(4);
    // assertFalse(mi1.isInRadius(mi2, 0));
    // assertTrue(mi1.isInRadius(mi2, 0));
    // }
    // @Test
    // public void testHitBoxConnected() {
    // MapItem mi1 = new MapItemX();
    // MapItem mi2 = new MapItemX();
    // mi1.getActor().setCenterX(0);
    // mi1.getActor().setCenterY(0);
    // mi2.getActor().setCenterX(2);
    // mi2.getActor().setCenterY(4);
    // }

    class MapItemX extends MapItem {

        public MapItemX() { super(null); }

    }
}

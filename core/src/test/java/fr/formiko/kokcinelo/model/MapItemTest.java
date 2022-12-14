package fr.formiko.kokcinelo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapItemTest extends Assertions {

    @Test
    public void testMapItem() {
        MapItem mi1 = new MapItemX(10, 10);
        mi1.getActor().setCenterX(0);
        mi1.getActor().setCenterY(0);
        assertNotNull(mi1.getActor());
    }
    @Test
    public void testIsInRadius() {
        MapItem mi1 = new MapItemX();
        MapItem mi2 = new MapItemX();
        mi1.getActor().setCenterX(0);
        mi1.getActor().setCenterY(0);
        mi2.getActor().setCenterX(2);
        mi2.getActor().setCenterY(4);
        assertFalse(mi1.isInRadius(mi2, -1000.145));
        assertFalse(mi1.isInRadius(mi2, 0));
        assertFalse(mi1.isInRadius(mi2, 3));
        assertFalse(mi1.isInRadius(mi2, 4));
        assertTrue(mi1.isInRadius(mi2, 5));
        assertTrue(mi1.isInRadius(mi2, 10000));
    }
    @Test
    public void testHitBoxConnected() {
        MapItem mi1 = new MapItemX();
        MapItem mi2 = new MapItemX(12, 12);
        mi1.getActor().setCenterX(0);
        mi1.getActor().setCenterY(0);
        mi2.getActor().setCenterX(2);
        mi2.getActor().setCenterY(4);
        // TODO set hitRadius & see Radius & test it.
    }

    class MapItemX extends MapItem {

        public MapItemX(int width, int height) { super(width, height); }
        public MapItemX() { this(10, 10); }
    }
}

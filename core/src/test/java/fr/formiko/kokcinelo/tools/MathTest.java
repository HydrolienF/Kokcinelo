package fr.formiko.kokcinelo.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathTest extends Assertions {
    @Test
    public void testGetDistanceBetweenPoints() { assertEquals(1, Math.getDistanceBetweenPoints(0, 0, 0, 1)); }
    @Test
    public void testGetDistanceBetweenPoints2() {
        assertTrue(1.69 < Math.getDistanceBetweenPoints(0, 2.7, 0, 1));
        assertTrue(1.71 > Math.getDistanceBetweenPoints(0, 2.7, 0, 1));
    }
    @Test
    public void testGetDistanceBetweenPoints3() {
        assertTrue(15.2 < Math.getDistanceBetweenPoints(1.7f, 9.5f, -3f, -5f));
        assertTrue(15.3 > Math.getDistanceBetweenPoints(1.7f, 9.5f, -3f, -5f));
    }
}

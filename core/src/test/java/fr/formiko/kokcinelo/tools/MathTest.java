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
    @Test
    public void testBetweenInt() {
        assertEquals(1, Math.between(0, 10, 1));
        assertEquals(2, Math.between(2, 10, 1));
        assertEquals(-2, Math.between(-6, -2, 100));
        assertEquals(-20, Math.between(-100, -20, 2));
    }
    @Test
    public void testBetweenLong() {
        assertEquals(1l, Math.between(0l, 10l, 1l));
        assertEquals(2l, Math.between(2l, 10l, 1l));
        assertEquals(-2l, Math.between(-6l, -2l, 100l));
    }
    @Test
    public void testBetweenFloat() {
        assertEquals(1.9f, Math.between(1.9f, 10f, 1.89f));
        assertEquals(10f, Math.between(1.9f, 10f, 11f));
        assertEquals(2f, Math.between(1.9f, 10f, 2f));
    }
    @Test
    public void testBetweenDouble() {
        assertEquals(1.9, Math.between(1.9, 10, -45678));
        assertEquals(10., Math.between(1.9, 10, 11));
        assertEquals(2., Math.between(1.9, 10., 2.));
    }
    @Test
    public void testMath() {
        new Math(); // no error;
    }
    @Test
    public void testBetweenByte() {
        assertEquals(5, Math.between((byte) 1, (byte) 10, (byte) 5));
        assertEquals(1, Math.between((byte) 1, (byte) 10, (byte) 0));
        assertEquals(10, Math.between((byte) 1, (byte) 10, (byte) 11));
    }
}

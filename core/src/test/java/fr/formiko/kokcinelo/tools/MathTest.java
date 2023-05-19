package fr.formiko.kokcinelo.tools;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.badlogic.gdx.math.Vector2;

class MathTest extends Assertions {
    @Test
    void testGetDistanceBetweenPoints() { assertEquals(1, Math.getDistanceBetweenPoints(0, 0, 0, 1)); }
    @Test
    void testGetDistanceBetweenPoints2() {
        assertTrue(1.69 < Math.getDistanceBetweenPoints(0, 2.7, 0, 1));
        assertTrue(1.71 > Math.getDistanceBetweenPoints(0, 2.7, 0, 1));
    }
    @Test
    void testGetDistanceBetweenPoints3() {
        assertTrue(15.2 < Math.getDistanceBetweenPoints(1.7f, 9.5f, -3f, -5f));
        assertTrue(15.3 > Math.getDistanceBetweenPoints(1.7f, 9.5f, -3f, -5f));
    }
    @Test
    void testBetweenInt() {
        assertEquals(1, Math.between(0, 10, 1));
        assertEquals(2, Math.between(2, 10, 1));
        assertEquals(-2, Math.between(-6, -2, 100));
        assertEquals(-20, Math.between(-100, -20, 2));
    }
    @Test
    void testBetweenLong() {
        assertEquals(1l, Math.between(0l, 10l, 1l));
        assertEquals(2l, Math.between(2l, 10l, 1l));
        assertEquals(-2l, Math.between(-6l, -2l, 100l));
    }
    @Test
    void testBetweenFloat() {
        assertEquals(1.9f, Math.between(1.9f, 10f, 1.89f));
        assertEquals(10f, Math.between(1.9f, 10f, 11f));
        assertEquals(2f, Math.between(1.9f, 10f, 2f));
    }
    @Test
    void testBetweenDouble() {
        assertEquals(1.9, Math.between(1.9, 10, -45678));
        assertEquals(10., Math.between(1.9, 10, 11));
        assertEquals(2., Math.between(1.9, 10., 2.));
    }
    @Test
    void testBetweenByte() {
        assertEquals(5, Math.between((byte) 1, (byte) 10, (byte) 5));
        assertEquals(1, Math.between((byte) 1, (byte) 10, (byte) 0));
        assertEquals(10, Math.between((byte) 1, (byte) 10, (byte) 11));
    }

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
            // just a point
            "10, 10, 10, 0, 20, 100, 20, 0, 0", // in top
            "10, 10, 10, 0, 0, 100, 0, 180, 180", // in bottom
            "10, 10, 10, 0, 0, 0, 100, 90, 90", // in left
            "10, 10, 10, 20, 0, 20, 100, 270, 270", // in rigth
            // more than one point
            "10, 10, 12, 0, 0, 0, 100, 56.45, 123.55", // in left
            "10, 10, 13, 0, 0, 0, 100, 50.28, 129.71", // in left

    })
    // @formatter:on
    void testGetSegmentIntersectionAngles(float xc, float yc, float radius, float xw1, float yw1, float xw2, float yw2, float angle1,
            float angle2) {
        List<Float> angles = Math.getSegmentIntersectionAngles(new Vector2(xc, yc), radius, new Vector2(xw1, yw1), new Vector2(xw2, yw2));
        assertEquals(2, angles.size());
        almostEqualsAngle(angle1, angles.get(0));
        almostEqualsAngle(angle2, angles.get(1));
    }

    @ParameterizedTest
    // @formatter:off
    @CsvSource({
            // 0 point
            "10, 10, 9, 0, 20, 100, 20, 0, 0", // in top
            "10, 10, 9, 0, 0, 100, 0, 180, 180", // in bottom
            "10, 10, 9, 0, 0, 0, 100, 90, 90", // in left
            "10, 10, 9, 20, 0, 20, 100, 270, 270", // in rigth

    })
    // @formatter:on
    void testGetSegmentIntersectionAnglesNoIntersection(float xc, float yc, float radius, float xw1, float yw1, float xw2, float yw2) {
        List<Float> angles = Math.getSegmentIntersectionAngles(new Vector2(xc, yc), radius, new Vector2(xw1, yw1), new Vector2(xw2, yw2));
        assertEquals(0, angles.size());
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

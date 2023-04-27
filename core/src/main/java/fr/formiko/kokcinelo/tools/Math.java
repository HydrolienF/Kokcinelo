package fr.formiko.kokcinelo.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.badlogic.gdx.math.Vector2;

/**
 * {@summary Math tools class with usefull static functions.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class Math {
    private Math() {}

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Return distance between 2 points.}
     * 
     * @param x1 x of 1a point
     * @param y1 y of 1a point
     * @param x2 x of 2a point
     * @param y2 y of 2a point
     * @return
     */
    public static double getDistanceBetweenPoints(int x1, int y1, int x2, int y2) {
        return getDistanceBetweenPoints((double) x1, (double) y1, (double) x2, (double) y2);
    }
    /**
     * {@summary Return distance between 2 points.}
     * 
     * @param x1 x of 1a point
     * @param y1 y of 1a point
     * @param x2 x of 2a point
     * @param y2 y of 2a point
     * @return
     */
    public static double getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        return getDistanceBetweenPoints((double) x1, (double) y1, (double) x2, (double) y2);
    }
    /**
     * {@summary Return distance between 2 points.}
     * 
     * @param x1 x of 1a point
     * @param y1 y of 1a point
     * @param x2 x of 2a point
     * @param y2 y of 2a point
     * @return
     */
    public static double getDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return java.lang.Math.sqrt(java.lang.Math.pow((y2 - y1), 2) + java.lang.Math.pow((x2 - x1), 2));
    }

    /**
     * {@summary Return a value in an interval.}<br>
     * max &#38; min are in the interval.
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @param val the value to test
     * @return val or a bound
     */
    public static int between(int min, int max, int val) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
    /**
     * {@summary Return a value in an interval.}<br>
     * max &#38; min are in the interval.
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @param val the value to test
     * @return val or a bound
     */
    public static long between(long min, long max, long val) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
    /**
     * {@summary Return a value in an interval.}<br>
     * max &#38; min are in the interval.
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @param val the value to test
     * @return val or a bound
     */
    public static byte between(byte min, byte max, byte val) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
    /**
     * {@summary Return a value in an interval.}<br>
     * max &#38; min are in the interval.
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @param val the value to test
     * @return val or a bound
     */
    public static float between(float min, float max, float val) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }
    /**
     * {@summary Return a value in an interval.}<br>
     * max &#38; min are in the interval.
     * 
     * @param min the minimum value
     * @param max the maximum value
     * @param val the value to test
     * @return val or a bound
     */
    public static double between(double min, double max, double val) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }

    public static List<Float> getSegmentIntersectionAngles(Vector2 circleCenter, float circleRadius, Vector2 pointA, Vector2 pointB) {
        return getCircleLineIntersectionPoint(pointA, pointB, circleCenter, circleRadius).stream()
                .map(intersection -> (intersection.sub(circleCenter).angleDeg() - 90f + 360f) % 360f).sorted().collect(Collectors.toList());
    }

    // cf https://stackoverflow.com/questions/13053061/circle-line-intersection-points
    public static List<Vector2> getCircleLineIntersectionPoint(Vector2 pointA, Vector2 pointB, Vector2 center, float radius) {
        float baX = pointB.x - pointA.x;
        float baY = pointB.y - pointA.y;
        float caX = center.x - pointA.x;
        float caY = center.y - pointA.y;

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = caX * caX + caY * caY - radius * radius;

        float pBy2 = bBy2 / a;
        float q = c / a;

        float disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        float tmpSqrt = (float) java.lang.Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector2 p1 = new Vector2(pointA.x - baX * abScalingFactor1, pointA.y - baY * abScalingFactor1);
        Vector2 p2;
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            p2 = p1;
        } else {
            p2 = new Vector2(pointA.x - baX * abScalingFactor2, pointA.y - baY * abScalingFactor2);
        }
        return Arrays.asList(p1, p2);
    }
}

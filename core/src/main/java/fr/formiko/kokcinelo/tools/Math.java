package fr.formiko.kokcinelo.tools;

/**
 * {@summary Math tools class with usefull static functions.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class Math {

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
     * @lastEditedVersion 2.5
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
     * @lastEditedVersion 2.5
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
     * @lastEditedVersion 2.5
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
     * @lastEditedVersion 2.5
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
     * @lastEditedVersion 2.5
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
}

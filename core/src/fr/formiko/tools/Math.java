package fr.formiko.tools;

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
}

package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;

/**
 * {@summary Screen class with fiew more funtion than the one provide by libGDX.}
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 1.2
 */
public class KScreen {
    protected int width;
    protected int height;
    protected List<Integer> times;

    /**
     * {@summary Initialize collections that need to be initialize.}
     */
    public KScreen() {
        times = new LinkedList<>(); // LinkedList because many add and few get.
    }

    /**
     * {@summary Return true if size have change.}
     * 
     * @param width  new width of the screen.
     * @param height new height of the screen.
     * @return true if the screen need to be resized.
     */
    public boolean needResize(int width, int height) {
        if ((width == 0 && height == 0) || (width == this.width && height == this.height))
            return false;
        this.width = width;
        this.height = height;
        return true;
    }
    /**
     * {@summary Display performances stats in log.}
     */
    public void displayPerf() {
        IntSummaryStatistics stats = times.stream().mapToInt(Integer::intValue).summaryStatistics();
        App.log(1, "PERFORMANCES",
                getClass().getSimpleName() + ": " + stats.getAverage() + " ms in average. (max: " + stats.getMax() + " ms)");
    }
}

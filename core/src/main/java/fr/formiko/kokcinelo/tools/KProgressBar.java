package fr.formiko.kokcinelo.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Null;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * {@summary A progress bar.}
 * All colors can be null and will be tranculent.
 * Update value with {@link #setProgress(float)}. Progress need to be in range 0.0f to 1.0f.
 * 
 * @author Hydrolien
 * @version 2.5
 * @since 2.5
 */
public class KProgressBar extends Actor {
    private ShapeDrawer shapeDrawer;
    protected @Null Color backgroundFullColor;
    protected @Null Color backgroundEmptyColor;
    protected @Null Color borderColor;
    protected float progress; // 0.0f to 1.0f

    /**
     * {@summary Main constructor.}
     * 
     * @param width                Width of the progress bar in pixel.
     * @param height               Height of the progress bar in pixel. If -1, height will be 1/10 of width.
     * @param backgroundFullColor  Color of the full part of the progress bar.
     * @param backgroundEmptyColor Color of the empty part of the progress bar.
     * @param borderColor          Color of the border of the progress bar.
     */
    public KProgressBar(int width, int height, @Null Color backgroundFullColor, @Null Color backgroundEmptyColor, @Null Color borderColor) {
        super();
        if (height == -1) {
            height = width / 10;
        }
        setSize(width, height);
        this.backgroundFullColor = backgroundFullColor;
        this.backgroundEmptyColor = backgroundEmptyColor;
        this.borderColor = borderColor;
    }

    public float getProgress() { return progress; }
    public void setProgress(float progress) { this.progress = progress; }

    /**
     * {@summary Draw the progress bar.}
     * Draw is done as filled rectangle for the full and empty sections, and empty rectangle for the border.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (shapeDrawer == null) {
            shapeDrawer = Shapes.createShapeDrawer(batch);
        }
        if (backgroundEmptyColor != null) {
            shapeDrawer.setColor(backgroundEmptyColor);
            shapeDrawer.filledRectangle(getX() + getWidth() * progress, getY(), getWidth() * (1 - progress), getHeight());
        }
        if (backgroundFullColor != null) {
            shapeDrawer.setColor(backgroundFullColor);
            shapeDrawer.filledRectangle(getX(), getY(), getWidth() * progress, getHeight());
        }
        if (borderColor != null) {
            shapeDrawer.setColor(borderColor);
            shapeDrawer.rectangle(getX(), getY(), getWidth(), getHeight(), 2);
        }
    }
}

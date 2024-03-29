package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.tools.KTexture;
import fr.formiko.kokcinelo.tools.Math;
import fr.formiko.kokcinelo.tools.Shapes;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;

/**
 * {@summary Represent the map &#38; overlay actor}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class MapActor extends Actor {
    private Texture texture;
    private Texture textureWithDark;
    private Set<Circle> toExcude;
    private static final float MAX_ROCK_RADIUS = 50f;
    private static final float MIN_ROCK_RADIUS = 10f;

    /**
     * {@summary Main constructor.}
     * 
     * @param width       width of this
     * @param height      height of this
     * @param color       color of this
     * @param withDetails if true, add some details to the map.
     * @param stones      number of stones to add to the map.
     * @param sticks      number of sticks to add to the map.
     * @param color2      color of the gradient.
     */
    public MapActor(float width, float height, Color color, boolean withDetails, int stones, int sticks, @Null Color color2) {
        toExcude = new HashSet<>();
        // setColor(color); // if we set color, map appear with some sort of colored filter.
        setWidth(width);
        setHeight(height);
        try {
            createTexture((int) width, (int) height, color, withDetails, stones, sticks, color2);
        } catch (Throwable e) {
            App.log(3, "constructor", "Error while creating texture: " + e.getMessage());
        }
        setOrigin(Align.center);
        setVisible(true);

        App.log(0, "constructor", "new MapActor: " + toString());
    }

    public MapActor(float width, float height, Color color, boolean withDetails, int stones, int sticks) {
        this(width, height, color, withDetails, stones, sticks, null);
    }

    public MapActor(float width, float height, Color color, boolean withDetails) { this(width, height, color, withDetails, 10, 20); }

    public MapActor(float width, float height, Color color, Color color2) { this(width, height, color, false, 0, 0, color2); }

    /**
     * {@summary Draw the texture that represent this.}
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthFunc(GL30.GL_LESS);
        batch.draw(getMaskedTexture(), getX(), getY(), getWidth() * getScaleX(), getHeight() * getScaleY());
    }
    /**
     * {@summary Standard toString that return important vars as String.}
     */
    @Override
    public String toString() {
        return "MapActor " + "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + ", " + getRotation() + ", "
                + getOriginX() + ", " + getOriginY() + ", " + getScaleX() + ", " + getScaleY() + "]";
    }
    /**
     * {@summary Return maskedTexture.}
     * Default texture is full of color.
     * It have a circle area to exclude.
     * 
     * @return texture without first circle area to exclude
     */
    private Texture getMaskedTexture() {
        if (toExcude.isEmpty()) {
            return texture;
        } else {
            if (textureWithDark == null) {
                // Circle c = null;
                // for (Circle circle : toExcude) {
                // c = circle;
                // break;
                // }
                Circle c = toExcude.iterator().next();
                long time = System.currentTimeMillis();
                textureWithDark = new Texture(getMaskPixmap((int) c.radius));
                // TODO try to call only once or reduce time to create texture.
                App.log(1, System.currentTimeMillis() - time + "ms to create textureWithDark");
            }
            return textureWithDark;
        }
    }
    /**
     * {@summary Create a texture with width, height &#38; color.}
     * 
     * @param width  width of texture
     * @param height height of texture
     * @param color  color of texture
     * @param stones number of stones to add
     */
    private void createTexture(int width, int height, Color color, boolean withDetails, int stones, int sticks, @Null Color color2) {
        Pixmap pixmap = Shapes.createPixmap(width, height, color, color2);
        if (withDetails) {
            drawStones(pixmap, width, height, stones);
            drawSticks(pixmap, width, height, sticks);
            // To save the pixmap to a file, uncomment the following line:
            // PixmapIO.writePNG(new FileHandle("screenshotX.png"), pixmap);// save pixmap to file
        }
        texture = new KTexture(pixmap);
        pixmap.dispose();
    }


    public void clearToExclude() { toExcude.clear(); }

    public void addToExclude(float x, float y, float radius) { toExcude.add(new Circle(x, y, radius)); }

    /**
     * {@summary Return pixmap without a centered circle of pixel.}
     * 
     * @return pixmap without a centered circle of pixel
     */
    private Pixmap getMaskPixmap(int radius) {
        final int blackLevel = 150; // [0; 255]
        final float egdeSize = 0.2f;

        Pixmap darkedArea = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        int xCenter = darkedArea.getWidth() / 2;
        int yCenter = darkedArea.getHeight() / 2;
        float edgeLength = radius * egdeSize;

        for (int x = 0; x < darkedArea.getWidth(); x++) {
            for (int y = 0; y < darkedArea.getHeight(); y++) {
                int distToCenter = (int) Math.getDistanceBetweenPoints(x, y, xCenter, yCenter);
                if (distToCenter > radius) {
                    darkedArea.drawPixel(x, y, blackLevel);
                } else if (distToCenter > radius - edgeLength) {
                    float nextToTheEdgess = 1f - (radius - distToCenter) / edgeLength;
                    darkedArea.drawPixel(x, y, (int) (blackLevel * nextToTheEdgess));
                }
            }
        }
        return darkedArea;
    }

    /**
     * {@summary Draw stones on pixmap.}
     * 
     * @param pixmap pixmap were to draw
     * @param width  width of the area to draw
     * @param height height of the area to draw
     * @param stones number of stones to draw
     */
    private void drawStones(Pixmap pixmap, int width, int height, int stones) {
        for (int i = 0; i < stones; i++) {
            pixmap.setColor(getRandomGrey());
            int radius = (int) ((random() * (MAX_ROCK_RADIUS - MIN_ROCK_RADIUS)) + MIN_ROCK_RADIUS);
            int x = (int) (random() * width);
            int y = (int) (random() * height);
            pixmap.fillCircle(x, y, radius);
            int substones = 1 + (int) (random() * 3);
            for (int j = 0; j < substones; j++) {
                pixmap.setColor(getRandomGrey());
                pixmap.fillCircle((int) (x + ((random() - 0.5) * 2.0 * radius)), (int) (y + ((random() - 0.5) * radius)),
                        (int) (radius / ((random() * 2) + 1.5)));
            }
        }
    }
    private static final int MAX_STIK_LENGTH = 400;
    private static final int MIN_STIK_LENGTH = 80;
    private static final int MIN_STIK_WIDTH = 5;
    private static final int MAX_STIK_WIDTH = 25;
    private static final int MAX_STIK_SUBSEGMENT = 1;
    private static final int MAX_STIK_SEGMENT = 4;
    private static final int MIN_STIK_SEGMENT = 2;
    /**
     * {@summary Draw woods sticks on pixmap.}
     * 
     * @param pixmap pixmap were to draw
     * @param width  width of the area to draw
     * @param height height of the area to draw
     * @param sticks number of sticks to draw
     */
    private void drawSticks(Pixmap pixmap, int width, int height, int sticks) {
        for (int i = 0; i < sticks; i++) {
            // TODO fill intercection with one more triangle.
            pixmap.setColor(getRandomBrown());
            float thikness = (float) ((random() * (MAX_STIK_WIDTH - MIN_STIK_WIDTH)) + MIN_STIK_WIDTH);
            float x = (float) (random() * width - thikness) + thikness / 2;
            float y = (float) (random() * height - thikness) + thikness / 2;
            int segments = (int) (random() * MAX_STIK_SEGMENT - MIN_STIK_SEGMENT) + MIN_STIK_SEGMENT;
            int subsegments = (int) (random() * MAX_STIK_SUBSEGMENT + 0.8);
            float rotation = (float) (random() * 360);
            float length = (float) ((random() * (MAX_STIK_LENGTH - MIN_STIK_LENGTH)) + MIN_STIK_LENGTH);
            drawStickBranch(pixmap, width, height, x, y, segments, subsegments, rotation, length, thikness);
        }
    }
    /**
     * {@summary Recurcive function to draw a stick.}
     * Most param are used for the next segment to with fiew lower variable.
     * 
     * @param pixmap   pixmap were to draw
     * @param width    width of the area to draw
     * @param height   height of the area to draw
     * @param x        x start of the branch
     * @param y        y start of the branch
     * @param segments segment left to draw
     * @param rotation rotation of this segment
     * @param length   length of this segement
     * @param thikness thikness of this segment
     */
    //@formatter:off
    private void drawStickBranch(Pixmap pixmap, int width, int height, float x, float y, int segments, int subsegments, float rotation, float length, float thikness) {
    //@formatter:on
        float thiknessModifier = (random() * 0.2f) + 0.6f;
        float lengthX = length * MathUtils.cos(rotation * MathUtils.degreesToRadians);
        float lengthY = length * MathUtils.sin((-1 * rotation) * MathUtils.degreesToRadians);
        float rotation2 = rotation - 90;
        float thiknessX = thikness * MathUtils.cos(rotation2 * MathUtils.degreesToRadians);
        float thiknessY = thikness * MathUtils.sin((-1 * rotation2) * MathUtils.degreesToRadians);

        int px = (int) Math.between(0, getWidth(), x);
        int pxl = (int) Math.between(0, getWidth(), x + lengthX);
        int pxt = (int) Math.between(0, getWidth(), x + thiknessX);
        int pxlt = (int) Math.between(0, getWidth(), x + (thiknessX * thiknessModifier) + lengthX);
        int py = (int) Math.between(0, getHeight(), y);
        int pyl = (int) Math.between(0, getHeight(), y + lengthY);
        int pyt = (int) Math.between(0, getHeight(), y + thiknessY);
        int pylt = (int) Math.between(0, getHeight(), y + (thiknessY * thiknessModifier) + lengthY);

        pixmap.fillTriangle(px, py, pxl, pyl, pxt, pyt);
        pixmap.fillTriangle(px, py, pxl, pyl, pxlt, pylt);
        pixmap.fillTriangle(pxt, pyt, pxl, pyl, pxlt, pylt);
        if (segments > 1) {
            drawStickBranch(pixmap, width, height, x + lengthX, y + lengthY, segments - 1, subsegments, rotation + (random() * 40) - 20,
                    length * (random() * 1.3f), thikness * thiknessModifier);
            for (int i = 0; i < subsegments; i++) {
                float modRotation = 50f + random() * 30f;
                if (random() > 0.5) {
                    modRotation *= -1f;
                }
                drawStickBranch(pixmap, width, height, x + lengthX * (0.6f + random() * 0.2f), y + lengthY * (0.6f + random() * 0.2f),
                        segments - 1, 0, rotation + modRotation, length * (random() * 0.6f), thikness * thiknessModifier);
            }
        }
    }

    private float random() { return (float) java.lang.Math.random(); }
    /**
     * {@summary Create a random grey.}
     * 
     * @return a random grey
     */
    private Color getRandomGrey() {
        float r = (float) (random() / 2.0 + 0.2);
        return new Color(r, r, r, 1f);
    }
    /**
     * {@summary Create a random brown.}
     * 
     * @return a random brown
     */
    private Color getRandomBrown() {
        return new Color((float) (random() * 0.2) + 0.35f, (float) (random() * 0.08) + 0.08f, (float) (random() * 0.05), 1f);
    }
}

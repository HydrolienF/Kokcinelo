package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.tools.Math;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

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
    private ShapeRenderer shapeRenderer;
    private static final float MAX_ROCK_RADIUS = 50f;
    private static final float MIN_ROCK_RADIUS = 10f;

    /**
     * {@summary Main constructor.}
     * 
     * @param width  width of this
     * @param height heigth of this
     * @param color  color of this
     */
    public MapActor(float width, float height, Color color) {
        toExcude = new HashSet<Circle>();
        // setColor(color); // if we set color, map appear with some sort of colored filter.
        setWidth(width);
        setHeight(height);
        createTexture((int) width, (int) height, color, 10);
        setOrigin(Align.center);
        setVisible(true);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    /**
     * {@summary Draw the texture that represent this.}
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthFunc(GL30.GL_LESS);
        batch.draw(getMaskedTexture(), getX(), getY(), getWidth(), getHeight());
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
        if (toExcude.size() == 0) {
            return texture;
        } else {
            if (textureWithDark == null) {
                Circle c = null;
                for (Circle circle : toExcude) {
                    c = circle;
                    break;
                }
                System.out.println("new textureWithDark");
                textureWithDark = new Texture(getMaskPixmap((int) c.radius));
            }
            return textureWithDark;
        }
    }
    /**
     * {@summary Create a texture with width, heigth &#38; color.}
     * 
     * @param width  width of texture
     * @param height heigth of texture
     * @param color  color of texture
     * @param rocks  number of rocks to add
     */
    private void createTexture(int width, int height, Color color, int rocks) {
        Pixmap pixmap = createPixmap(width, height, color);
        drawRocks(pixmap, width, height, rocks);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }
    /**
     * {@summary Create a pixmap with a single color.}
     * 
     * @param width  width of pixmap
     * @param height heigth of pixmap
     * @param color  color of pixmap
     */
    private Pixmap createPixmap(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        return pixmap;
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
        int xCenter = (int) (darkedArea.getWidth() / 2);
        int yCenter = (int) (darkedArea.getHeight() / 2);
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
     * {@summary Draw rocks on pixmap.}
     * 
     * @param pixmap pixmap were to draw
     * @param width  width of the area to draw
     * @param height height of the area to draw
     * @param rocks  number of rocks to draw
     */
    private void drawRocks(Pixmap pixmap, int width, int height, int rocks) {
        for (int i = 0; i < rocks; i++) {
            pixmap.setColor(getRandomGrey());
            int radius = (int) ((random() * (MAX_ROCK_RADIUS - MIN_ROCK_RADIUS)) + MIN_ROCK_RADIUS);
            int x = (int) (random() * width);
            int y = (int) (random() * height);
            pixmap.fillCircle(x, y, radius);
            int subrocks = 1 + (int) (random() * 3);
            for (int j = 0; j < subrocks; j++) {
                pixmap.setColor(getRandomGrey());
                pixmap.fillCircle((int) (x + ((random() - 0.5) * 2.0 * radius)), (int) (y + ((random() - 0.5) * radius)),
                        (int) ((double) radius / ((random() * 2) + 1.5)));
            }
        }
    }
    private double random() { return java.lang.Math.random(); }
    /**
     * {@summary Create a random grey.}
     * 
     * @return a random grey
     */
    private Color getRandomGrey() {
        float r = (float) (random() / 2.0 + 0.2);
        return new Color(r, r, r, 1f);
    }
}

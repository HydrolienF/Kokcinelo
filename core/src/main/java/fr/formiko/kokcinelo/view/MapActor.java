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
    /**
     * {@summary Main constructor.}
     * 
     * @param width  width of this
     * @param height heigth of this
     * @param color  color of this
     */
    public MapActor(float width, float height, Color color) {
        toExcude = new HashSet<Circle>();
        setColor(color);
        setWidth(width);
        setHeight(height);
        createTexture((int) width, (int) height, color);
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
            Circle c = null;
            for (Circle circle : toExcude) {
                c = circle;
                break;
            }
            if (textureWithDark == null) {
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
     */
    private void createTexture(int width, int height, Color color) {
        Pixmap pixmap = createPixmap(width, height, color);
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
}

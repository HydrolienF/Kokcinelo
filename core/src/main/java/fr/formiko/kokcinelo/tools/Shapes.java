package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * {@summary Tools to get Shapes that ShapeRenderer is not able to create.}
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
public class Shapes {
    private static ShapeRenderer shapeRenderer;
    /**
     * {@summary Draw a sky gradient.}
     * 
     * @param width  width of the sky rectangle
     * @param heigth height of the sky rectangle
     * @param ligth  ligth of the sky bewteen 0 and 1
     */
    public static void drawSky(int width, int heigth, float ligth) {
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
            shapeRenderer.setAutoShapeType(true);
        }
        // draw blue sky gradient
        shapeRenderer.begin();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        Color topColor = new Color(0, 0.4f * ligth, 1f * ligth, 1);
        Color bottomColor = new Color(0, 0.8f * ligth, 1f * ligth, 1);
        shapeRenderer.rect(0, 0, width, heigth, bottomColor, bottomColor, topColor, topColor);
        shapeRenderer.end();
    }
    /**
     * {@summary Return a circle with a thik border.}
     * 
     * @param radius     radius of the circle
     * @param edgeLength length of the edge of the circle
     * @param color      color of the circle
     * @return a Pixmap with the circle in it
     */
    public static Texture getCircle(int radius, int edgeLength, int color) {
        Pixmap pm = getCirclePixmap(radius, edgeLength, color);
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }
    public static Texture getCircle(int radius, Color color) { return getCircle(radius, radius + 1, Color.rgba8888(color)); }

    /**
     * {@summary Return a circle with a thik border.}
     * 
     * @param radius     radius of the circle
     * @param edgeLength length of the edge of the circle
     * @param color      color of the circle
     * @return a Pixmap with the circle in it
     */
    public static Texture getCircle(int radius, int edgeLength, Color color) {
        return getCircle(radius, edgeLength, Color.rgba8888(color));
    }

    /**
     * {@summary Return a texture that fit into a circle.}
     * 
     * @param radius  radius of the circle
     * @param color   color of the circle
     * @param texture texture to put into the circle
     * @param zoom    zoom of the texture
     * @return a texture that fit into a circle
     */
    public static Texture getCircledTexture(int radius, Color color, Texture texture, float zoom) {
        App.log(0, "Start to create circled texture.");
        Pixmap pixmap = getCirclePixmap(radius, radius + 1, Color.rgba8888(color));

        Pixmap texturePixmap = textureToPixmap(texture);
        int size = java.lang.Math.max(texturePixmap.getWidth(), texturePixmap.getHeight());
        int xOffset = (size - texturePixmap.getWidth()) / 2;
        int yOffset = (size - texturePixmap.getHeight()) / 2;
        // make square pixmap
        Pixmap squarePixmap;
        if (xOffset != 0 || yOffset != 0) {
            squarePixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
            squarePixmap.drawPixmap(texturePixmap, 0, 0, texturePixmap.getWidth(), texturePixmap.getHeight(), xOffset, yOffset,
                    texturePixmap.getWidth(), texturePixmap.getHeight());
        } else {
            squarePixmap = texturePixmap;
        }

        // resize image
        Pixmap texturePixmapSized = new Pixmap((int) (pixmap.getWidth() * zoom), (int) (pixmap.getHeight() * zoom), Pixmap.Format.RGBA8888);
        texturePixmapSized.drawPixmap(squarePixmap, 0, 0, squarePixmap.getWidth(), squarePixmap.getHeight(), 0, 0,
                (int) (pixmap.getWidth() * zoom), (int) (pixmap.getHeight() * zoom));
        // Pixmap texturePixmapSized = resize(squarePixmap, (int) (squarePixmap.getWidth() * zoom), (int) (squarePixmap.getHeight() * zoom),
        // true);

        // draw center circle of the image
        int xCenter = (int) (pixmap.getWidth() / 2);
        int yCenter = (int) (pixmap.getHeight() / 2);
        xOffset = (int) (pixmap.getWidth() / 2 - texturePixmapSized.getWidth() / 2);
        yOffset = (int) (pixmap.getHeight() / 2 - texturePixmapSized.getHeight() / 2);

        for (int x = 0; x < texturePixmapSized.getWidth(); x++) {
            for (int y = 0; y < texturePixmapSized.getHeight(); y++) {
                int distToCenter = (int) Math.getDistanceBetweenPoints(xOffset + x, yOffset + y, xCenter, yCenter);
                // TODO in [distToCenter-0.5, distToCenter+0.5] color should be with alpha to make a smooth border
                if (distToCenter <= radius) {
                    pixmap.drawPixel(xOffset + x, yOffset + y, texturePixmapSized.getPixel(x, y));
                }
            }
        }
        App.log(0, "End to create circled texture.");
        Texture t = new Texture(pixmap);
        texturePixmap.dispose();
        pixmap.dispose();
        texturePixmapSized.dispose();
        return t;
    }

    /**
     * {@summary Return a Pixmap that include a circle.}
     * 
     * @param radius     radius of the circle
     * @param edgeLength size of the border line of the circle
     * @param color      color of the circle
     * @return a Pixmap that include a circle
     */
    private static Pixmap getCirclePixmap(int radius, int edgeLength, int color) {
        App.log(0, "Start generate circle");
        Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
        int xCenter = (int) (pixmap.getWidth() / 2);
        int yCenter = (int) (pixmap.getHeight() / 2);

        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                int distToCenter = (int) Math.getDistanceBetweenPoints(x, y, xCenter, yCenter);
                // TODO in [distToCenter-0.5, distToCenter+0.5] color should be with alpha to make a smooth border
                if (distToCenter > radius - edgeLength && distToCenter <= radius) {
                    pixmap.drawPixel(x, y, color);
                }
            }
        }
        App.log(0, "End generate circle");
        return pixmap;
    }

    /**
     * Resize a Texture.
     * 
     * @param in        texture to resize
     * @param outWidth  new width
     * @param outHeigth new height
     * @return resized texture
     */
    public static Texture resize(Texture in, int outWidth, int outHeigth) {
        return new Texture(resize(textureToPixmap(in), outWidth, outHeigth));
    }
    /**
     * Resize a Pixmap.
     * 
     * @param in        Pixmap to resize
     * @param outWidth  new width
     * @param outHeigth new height
     * @return resized Pixmap
     */
    public static Pixmap resize(Pixmap inPm, int outWidth, int outHeigth) {
        Pixmap outPm = new Pixmap(outWidth, outHeigth, Pixmap.Format.RGBA8888);
        outPm.drawPixmap(inPm, 0, 0, inPm.getWidth(), inPm.getHeight(), 0, 0, outWidth, outHeigth);
        inPm.dispose();
        return outPm;
    }


    /***
     * Add a black border over the shapes in a texture.
     * For each pixel It check if the 4 next pixels are colored. If they are it, save location to be colored.
     * For each location to color, it place a black pixel.
     * 
     * @param in Texture to outline
     * @return a new Texture with a black border over the shapes
     */
    public static Texture outLine(Texture in) {
        Pixmap pm = outLine(textureToPixmap(in));
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    /**
     * Add a black border over the shapes in a texture.
     * For each pixel It check if the 4 next pixels are colored. If they are it, save location to be colored.
     * For each location to color, it place a black pixel.
     * 
     * @param in PixelMap to outline
     * @return a new Pixmap with a black border over the shapes
     */
    public static Pixmap outLine(Pixmap inPm) {
        Set<Vector2> locationsToColor = new HashSet<Vector2>();
        for (int x = 0; x < inPm.getWidth(); x++) {
            for (int y = 0; y < inPm.getHeight(); y++) {
                // If one of the 4 next pixels is colored, it save the location to be colored.
                if (((x > 0 && inPm.getPixel(x - 1, y) != 0) || (x < inPm.getWidth() - 1 && inPm.getPixel(x + 1, y) != 0)
                        || (y > 0 && inPm.getPixel(x, y - 1) != 0) || (y < inPm.getHeight() - 1 && inPm.getPixel(x, y + 1) != 0))
                        && ((x > 0 && inPm.getPixel(x - 1, y) == 0) || (x < inPm.getWidth() - 1 && inPm.getPixel(x + 1, y) == 0)
                                || (y > 0 && inPm.getPixel(x, y - 1) == 0) || (y < inPm.getHeight() - 1 && inPm.getPixel(x, y + 1) == 0))) {
                    locationsToColor.add(new Vector2(x, y));
                }
            }
        }
        int color = 255;
        for (Vector2 vector2 : locationsToColor) {
            inPm.drawPixel((int) vector2.x, (int) vector2.y, color);
        }
        return inPm;
    }

    /**
     * Return the pixmap of a texture.
     * 
     * @param in texture
     * @return the pixmap of a texture
     */
    private static Pixmap textureToPixmap(Texture in) {
        // get image as pixmap
        if (!in.getTextureData().isPrepared()) {
            in.getTextureData().prepare();
        }
        return in.getTextureData().consumePixmap();
    }

}

package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.model.Creature;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * {@summary Tools to get Shapes that ShapeRenderer is not able to create.}
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 0.2
 */
public class Shapes {
    private static ShapeRenderer shapeRenderer;
    private static ShapeDrawer shapeDrawer;
    private static @Null Texture outCircleTexture;

    private Shapes() {}

    public static ShapeRenderer getShapeRenderer() {
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
            shapeRenderer.setAutoShapeType(true);
        }
        return shapeRenderer;
    }
    public static ShapeDrawer getShapeDrawer() {
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        if (shapeDrawer == null) {
            shapeDrawer = createShapeDrawer(batch);
        }
        return shapeDrawer;
    }
    /**
     * {@summary Draw a gradiant over a pixmap.}
     * 
     * @param toDraw      Pixmap to draw
     * @param topColor    color of the top
     * @param bottomColor color of the bottom
     */
    // TODO test if it's better createPixmap() with 2 colors
    public static void drawGradientOnPixmap(Pixmap toDraw, Color topColor, Color bottomColor) {
        shapeRenderer = getShapeRenderer();
        shapeRenderer.begin();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, toDraw.getWidth(), toDraw.getHeight(), bottomColor, bottomColor, topColor, topColor);
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
        // texturePixmapSized.setFilter(Pixmap.Filter.NearestNeighbour); // worst than bilinear (the default & only other option)
        texturePixmapSized.drawPixmap(squarePixmap, 0, 0, squarePixmap.getWidth(), squarePixmap.getHeight(), 0, 0,
                (int) (pixmap.getWidth() * zoom), (int) (pixmap.getHeight() * zoom));
        // Pixmap texturePixmapSized = resize(squarePixmap, (int) (squarePixmap.getWidth() * zoom), (int) (squarePixmap.getHeight() * zoom),
        // true);

        // draw black outline
        texturePixmapSized = outLine(texturePixmapSized);

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
     * @param outheight new height
     * @return resized texture
     */
    public static Texture resize(Texture in, int outWidth, int outheight) {
        return new Texture(resize(textureToPixmap(in), outWidth, outheight));
    }
    /**
     * Resize a Pixmap.
     * 
     * @param inPm      Pixmap to resize
     * @param outWidth  new width
     * @param outheight new height
     * @return resized Pixmap
     */
    public static Pixmap resize(Pixmap inPm, int outWidth, int outheight) {
        Pixmap outPm = new Pixmap(outWidth, outheight, Pixmap.Format.RGBA8888);
        outPm.drawPixmap(inPm, 0, 0, inPm.getWidth(), inPm.getHeight(), 0, 0, outWidth, outheight);
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
     * @param inPm PixelMap to outline
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
     * {@summary Create a pixmap with a single color.}
     * 
     * @param width  width of pixmap
     * @param height height of pixmap
     * @param color  color of pixmap
     * @param color2 2a color of pixmap for gradient (optional)
     */
    public static Pixmap createPixmap(int width, int height, Color color, @Null Color color2) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        if (color2 != null) {
            for (int y = 0; y < height; y++) { // draw line with mixed color.
                pixmap.setColor(new Color(color.r * ((float) y / height) + color2.r * (1 - ((float) y / height)),
                        color.g * ((float) y / height) + color2.g * (1 - ((float) y / height)),
                        color.b * ((float) y / height) + color2.b * (1 - ((float) y / height)),
                        color.a * ((float) y / height) + color2.a * (1 - ((float) y / height))));
                pixmap.drawLine(0, y, width, y);
            }
        } else {
            pixmap.setColor(color);
            pixmap.fillRectangle(0, 0, width, height);
        }
        return pixmap;
    }

    public static ShapeDrawer createShapeDrawer(Batch batch) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap); // remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
        return new ShapeDrawer(batch, region);
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

    /**
     * {@summary Return pixmap without a centered circle of pixel.}
     *
     * @return pixmap without a centered circle of pixel
     */
    public static Pixmap getMaskPixmap(float radius, float width, float height) {
        final int blackLevel = 255; // [0; 255]
        final float egdeSize = 0.2f;

        Pixmap darkedArea = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA8888);
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
     * {@summary Return a sprite that fit into a circle.}
     * 
     * @param radius    radius of the circle
     * @param color     color of the circle
     * @param creatures list of creatures to draw
     * @return a sprite that fit into a circle
     */
    public static Sprite getCircledSprite(int radius, Color color, Creature... creatures) {
        SpriteBatch spriteBatch = new SpriteBatch();
        Stage stage = new Stage(new ScreenViewport());
        FrameBuffer frameBuffer = new FrameBuffer(Format.RGBA8888, radius * 2, radius * 2, false);

        int k = 0;
        for (Creature creature : creatures) {
            stage.addActor(creature.getActor());
            creature.setMovingSpeed(0);
            if (creatures.length == 1) {
                creature.setCenter(radius, radius);
            } else {
                if (k == 0) {
                    creature.setRotation(-90);
                } else {
                    creature.setRotation(90);
                }
                creature.setCenter(radius * 2 * k, radius);
            }
            creature.setZoom(creature.getZoom() * (radius / 100f));
            k++;
        }

        frameBuffer.bind();
        spriteBatch.begin();
        ScreenUtils.clear(color);
        // draw masked
        stage.act();
        stage.draw();

        // draw mask
        spriteBatch.flush();
        spriteBatch.setBlendFunctionSeparate(GL30.GL_ZERO, GL30.GL_ONE, GL30.GL_ZERO, GL30.GL_ONE_MINUS_SRC_ALPHA);
        spriteBatch.draw(getOutCircleMask(radius), 0, 0);
        spriteBatch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch.end();
        frameBuffer.end();

        Texture texture = frameBuffer.getColorBufferTexture();
        Sprite sprite = new Sprite(texture);
        sprite.flip(false, true);
        return sprite;
    }
    /**
     * {@summary Return a mask of outside a circle.}
     * It use a static texture to avoid to create a new texture each time.
     * 
     * @param radius radius of the circle
     */
    private static Texture getOutCircleMask(float radius) {
        if (outCircleTexture == null || outCircleTexture.getWidth() != radius * 2) {
            if (outCircleTexture != null)
                outCircleTexture.dispose();
            outCircleTexture = new Texture(getOutCirclePixmap(radius));
        }
        return outCircleTexture;
    }
    /**
     * {@summary Return a mask of outside a circle as a Pixmap.}
     * It will create a new pixmap each time.
     * 
     * @param radius radius of the circle
     */
    private static Pixmap getOutCirclePixmap(float radius) {
        Pixmap pixmap = new Pixmap((int) (radius * 2), (int) (radius * 2), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        for (int i = 0; i < pixmap.getWidth(); i++) {
            for (int j = 0; j < pixmap.getHeight(); j++) {
                if (Math.getDistanceBetweenPoints(i, j, radius, radius) > radius) {
                    pixmap.drawPixel(i, j);
                }
            }
        }
        return pixmap;
    }

    /**
     * {@summary Return a white background with transparency.}
     * 
     * @param alpha alpha of the background
     * @return a white background
     */
    public static Drawable getWhiteBackground(float alpha) {
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(new Color(1f, 1f, 1f, alpha));
        pixmap.fill();
        Drawable drawable = new Image(new Texture(pixmap)).getDrawable();
        pixmap.dispose();
        return drawable;
    }
    /**
     * {@summary Return a white background with transparency.}
     * 
     * @return a white background
     */
    public static Drawable getWhiteBackground() { return getWhiteBackground(0.3f); }

    public static Drawable getOveredRectangle(int width, int height, Color backgroundColor, int borderWidth, @Null Color borderColor) {
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        pixmap.setColor(backgroundColor);
        pixmap.fill();
        if (borderColor != null) {
            pixmap.setColor(borderColor);
            pixmap.fillRectangle(0, 0, borderWidth, height);
            pixmap.fillRectangle(0, 0, width, borderWidth);
            pixmap.fillRectangle(borderWidth, height - borderWidth, width, height);
            pixmap.fillRectangle(width - borderWidth, borderWidth, width, height);
        }
        Drawable drawable = new Image(new Texture(pixmap)).getDrawable();
        pixmap.dispose();
        return drawable;
    }
    public static Drawable getRectangle(int width, int height, Color color) { return getOveredRectangle(width, height, color, 1, null); }

}

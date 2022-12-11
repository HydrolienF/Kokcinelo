package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.MapItem;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

/**
 * {@summary Actor that represent a MapItem.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class MapItemActor extends Group {
    private static Map<String, TextureRegion> textureRegionMap;
    private String textureName;
    private MapItem mapItem;
    private static boolean showZone = false;
    private ShapeRenderer shapeRenderer;
    /**
     * {@summary Main constructor.}
     * 
     * @param textureName name of the texture to use to represent this Actor
     * @param mapItem     MapItem represent by this
     */
    public MapItemActor(String textureName, MapItem mapItem) {
        this.textureName = textureName;
        this.mapItem = mapItem;
        if (textureRegionMap == null) {
            textureRegionMap = new HashMap<String, TextureRegion>();
        }
        if (!textureRegionMap.containsKey(textureName)) {
            if (Gdx.files != null && textureName != null) {
                textureRegionMap.put(textureName, new TextureRegion(new Texture(Gdx.files.internal("images/" + textureName + ".png"))));
            }
        }
        if (getTextureRegion() != null) {
            setSize(getTextureRegion().getRegionWidth(), getTextureRegion().getRegionHeight());
            setOrigin(Align.center);
        }
    }
    public MapItemActor(MapItem mapItem, int width, int height) {
        this(null, mapItem);
        setSize(width, height);
        setOrigin(Align.center);
    }
    /**
     * {@summary Draw this actor texture and if showZone draw all debug info.}
     * Debug info are represent as circle for visionRadius &#38; hitRadius.
     * 
     * @param batch       batch were to draw
     * @param parentAlpha alpha of the parent to draw at same alpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (getTextureRegion() == null) {
            return;
        }
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (!(this instanceof MapItemActorAnimate)) {
            batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
                    getRotation());
        }

        if (mapItem instanceof Creature && showZone && ((Creature) mapItem).getVisionRadius() > 0) {
            batch.end();
            Creature c = (Creature) mapItem;
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            if (shapeRenderer == null) {
                shapeRenderer = new ShapeRenderer();
            }
            shapeRenderer.setProjectionMatrix(GameScreen.getCamera().combined);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(new Color(0f, 0f, 1f, parentAlpha * 1f));
            shapeRenderer.circle(getCenterX(), getCenterY(), (float) c.getVisionRadius());
            shapeRenderer.setColor(new Color(1f, 0f, 0f, parentAlpha * 1f));
            shapeRenderer.circle(getCenterX(), getCenterY(), (float) c.getHitRadius());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL30.GL_BLEND);
            batch.begin();
        }
    }
    /**
     * {@summary Standard toString that return important vars as String.}
     */
    @Override
    public String toString() {
        return "MapItemActor " + "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + ", " + getRotation() + ", "
                + getOriginX() + ", " + getOriginY() + ", " + getScaleX() + ", " + getScaleY() + "]";
    }

    // personaliseds functions -------------------------------------------------

    public void setZoom(float zoom) { setScale(zoom, zoom); }
    /**
     * {@summary Set center loaction to a random loaction}
     * 
     * @param maxX max value of x
     * @param maxY max value of y
     */
    public void setRandomLoaction(float maxX, float maxY) {
        setCenterX((float) Math.random() * maxX);
        setCenterY((float) Math.random() * maxY);
    }
    public float getCenterX() { return getX() + getWidth() / 2; }
    public float getCenterY() { return getY() + getHeight() / 2; }
    public void setCenterX(float x) { setX(x - getWidth() / 2); }
    public void setCenterY(float y) { setY(y - getHeight() / 2); }
    /**
     * {@summary Move in x &#38; y}
     * 
     * @param x x
     * @param y y
     */
    public void translate(float x, float y) {
        setX(getX() + x);
        setY(getY() + y);
    }
    /**
     * {@summary Move in the facing direction.}
     * 
     * @param distance distance to move
     */
    public void moveFront(float distance) {
        float facingAngle = getRotation() + 90;
        translate((float) (distance * Math.cos(Math.toRadians(facingAngle))), (float) (distance * Math.sin(Math.toRadians(facingAngle))));
    }

    /**
     * {@summary move Creature location between the rectangle 0,0,maxX,maxY if needed.}
     * 
     * @param maxX the max x for the creature
     * @param maxY the max y for the creature
     * @return true if Creature have been move
     */
    public boolean moveIn(float maxX, float maxY) {
        boolean haveMove = false;
        if (getCenterX() > maxX) {
            setCenterX(maxX);
            haveMove = true;
        } else if (getCenterX() < 0) {
            setCenterX(0f);
            haveMove = true;
        }
        if (getCenterY() > maxY) {
            setCenterY(maxY);
            haveMove = true;
        } else if (getCenterY() < 0) {
            setCenterY(0f);
            haveMove = true;
        }
        return haveMove;
    }

    // private -----------------------------------------------------------------
    private TextureRegion getTextureRegion() { return textureRegionMap.get(textureName); }

    // private Pixmap getDefaultPixmap(int width, int height) {
    // Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
    // pixmap.setColor(new Color(1, 0, 1, 1));
    // pixmap.fillRectangle(0, 0, width, height);
    // return pixmap;
    // }
}

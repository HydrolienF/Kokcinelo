package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.Controller;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class MapItemActor extends Actor {
    private static Map<String, TextureRegion> textureRegionMap;
    private String textureName;
    private MapItem mapItem;
    private static boolean showZone = true;
    private ShapeRenderer shapeRenderer;
    int i = 0;

    public MapItemActor(String textureName, MapItem mapItem) {
        this.textureName = textureName;
        this.mapItem = mapItem;
        if (textureRegionMap == null) {
            textureRegionMap = new HashMap<String, TextureRegion>();
        }
        if (!textureRegionMap.containsKey(textureName)) {
            textureRegionMap.put(textureName, new TextureRegion(new Texture(Gdx.files.internal("images/" + textureName + ".png"))));
        }
        setBounds(getTextureRegion().getRegionX(), getTextureRegion().getRegionY(), getTextureRegion().getRegionWidth(),
                getTextureRegion().getRegionHeight());
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
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation());
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
            shapeRenderer.circle(getX() + getWidth() / 2, getY() + getHeight() / 2, (float) c.getVisionRadius());
            shapeRenderer.setColor(new Color(1f, 0f, 0f, parentAlpha * 1f));
            shapeRenderer.circle(getX() + getWidth() / 2, getY() + getHeight() / 2, (float) c.getHitRadius());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL30.GL_BLEND);
            batch.begin();
        }
    }

    @Override
    public String toString() {
        return "MapItemActor " + "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + ", " + getRotation() + ", "
                + getOriginX() + ", " + getOriginY() + ", " + getScaleX() + ", " + getScaleY() + "]";
    }

    // personaliseds functions -------------------------------------------------

    public void setZoom(float zoom) { setScale(zoom, zoom); }
    public void setRandomLoaction(float maxX, float maxY) {
        setCenterX(Controller.getRandom().nextFloat(maxX));
        setCenterY(Controller.getRandom().nextFloat(maxY));
    }
    public float getCenterX() { return getX() + getWidth() / 2; }
    public float getCenterY() { return getY() + getHeight() / 2; }
    public void setCenterX(float x) { setX(x - getWidth() / 2); }
    public void setCenterY(float y) { setY(y - getHeight() / 2); }
    public void translate(float x, float y) {
        setX(getX() + x);
        setY(getY() + y);
    }
    public void moveFront(float distance) {
        float angle = getRotation() + 90;
        translate((float) (distance * Math.cos(Math.toRadians(angle))), (float) (distance * Math.sin(Math.toRadians(angle))));
    }

    /**
     * {@summary move Creature location between the rectangle 0,0,maxX,maxY if needed.}
     * 
     * @param maxX the max x for the creature
     * @param maxY the max y for the creature
     */
    public void moveIn(float maxX, float maxY) {
        if (getCenterX() > maxX) {
            setCenterX(maxX);
        } else if (getCenterX() < 0) {
            setCenterX(0f);
        }
        if (getCenterY() > maxY) {
            setCenterY(maxY);
        } else if (getCenterY() < 0) {
            setCenterY(0f);
        }
    }

    // private -----------------------------------------------------------------

    private TextureRegion getTextureRegion() { return textureRegionMap.get(textureName); }
}

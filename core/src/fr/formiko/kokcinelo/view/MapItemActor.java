package fr.formiko.kokcinelo.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import fr.formiko.kokcinelo.Controller;

public class MapItemActor extends Actor {
    private static Map<String, TextureRegion> textureRegionMap;
    private String textureName;

    public MapItemActor(String textureName) {
        this.textureName = textureName;
        if (textureRegionMap == null) {
            textureRegionMap = new HashMap<String, TextureRegion>();
        }
        if (!textureRegionMap.containsKey(textureName)) {
            textureRegionMap.put(textureName,
                    new TextureRegion(new Texture(Gdx.files.internal("images/" + textureName + ".png"))));
        }
        setBounds(getTextureRegion().getRegionX(), getTextureRegion().getRegionY(), getTextureRegion().getRegionWidth(),
                getTextureRegion().getRegionHeight());
        setOrigin(Align.center);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public String toString() {
        return "MapItemActor " + "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + ", "
                + getRotation() + "]";
    }

    // personaliseds functions -------------------------------------------------

    public void setZoom(float zoom) {
        setScale(zoom, zoom);
    }

    public void setRandomLoaction(float maxX, float maxY) {
        setX(Controller.getRandom().nextFloat(maxX));
        setY(Controller.getRandom().nextFloat(maxY));
    }

    // private -----------------------------------------------------------------

    private TextureRegion getTextureRegion() {
        return textureRegionMap.get(textureName);
    }
}

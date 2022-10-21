package fr.formiko.kokcinelo.view;

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

public class MapActor extends Actor {
    private Texture texture;
    private Set<Circle> toExcude;
    private ShapeRenderer shapeRenderer;
    private Color color;
    private static final int blackPixel = new Color(1f, 0f, 0f, 0f).toIntBits();

    public MapActor(float width, float height, Color color) {
        toExcude = new HashSet<Circle>();
        this.color=color;
        setWidth(width);
        setHeight(height);
        createTexture((int) width, (int) height, color);
        setOrigin(Align.center);
        setVisible(true);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthFunc(GL30.GL_LESS);
        batch.draw(getMaskedTexture(), getX(), getY(), getWidth(), getHeight());
    }
    
    @Override
    public String toString() {
        return "MapActor " + "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + ", "
                + getRotation() + ", " + getOriginX() + ", " + getOriginY() + ", " + getScaleX() + ", " + getScaleY()
                + "]";
    }
    private Texture getMaskedTexture(){
        if(toExcude.size()==0){
            return texture;
        }else{
            Pixmap darkedArea = new Pixmap(texture.getWidth(), texture.getHeight(), Pixmap.Format.RGBA8888);
            // Pixmap pixmap = createPixmap(texture.getWidth(), texture.getHeight(), color);
            Pixmap toRemove = getPixmapToRemove();
            /* Decide the color of each pixel. */
            for (int x = 0; x < toRemove.getWidth(); x++) {
                for (int y = 0; y < toRemove.getHeight(); y++) {
                    if(toRemove.getPixel(x, y) == 0.0){
                        darkedArea.drawPixel(x, y, blackPixel);
                    }
                }
            }
            Texture textureTemp = new Texture(darkedArea);
            darkedArea.dispose();
            toRemove.dispose();
            return textureTemp;
        }
    }
    private Pixmap getPixmapToRemove(){
        Pixmap toRemove = new Pixmap(texture.getWidth(), texture.getHeight(), Pixmap.Format.RGBA8888);
        /* This setting lets us overwrite the pixels' transparency. */ //it seem's to be useless.
        toRemove.setBlending(null);
        toRemove.setColor(new Color(1f, 1f, 1f, 1f));
        for (Circle circle : toExcude) {
            toRemove.fillCircle((int)circle.x, (int)circle.y, (int)circle.radius);
        }
        return toRemove;
    }

    private void createTexture(int width, int height, Color color) {
        Pixmap pixmap = createPixmap(width, height, color);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    private Pixmap createPixmap(int width, int height, Color color){
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        return pixmap;
    }

    public void clearToExclude(){
        toExcude.clear();
    }
    public void addToExclude(float x, float y, float radius){
        toExcude.add(new Circle(x,y,radius));
    }
}

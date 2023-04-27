package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.usual.Chrono;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * {@summary Group of actor able to display moving background.}
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 0.3
 */
public class EnvironmentMenuScreen extends Group {
    private final Chrono chrono;
    private float skyPercent = 0.5f;
    private boolean withGrass;
    private MapActor[] grass;
    private MapActor[] sky;
    private OrthographicCamera camera;
    private ShapeDrawer shapeDrawer;

    /**
     * {@summary Create the environment.}
     * 
     * @param camera The camera to use
     */
    public EnvironmentMenuScreen(OrthographicCamera camera) {
        this.camera = camera;
        chrono = new Chrono();
        chrono.start();
        withGrass = true;
        int heigth = 4000;
        float chunkSize = heigth * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        setSize(2 * chunkSize, heigth);
        grass = new MapActor[2];
        sky = new MapActor[2];
        for (int i = 0; i < 2; i++) {

            grass[i] = new MapActor(chunkSize, getHeight() * (1 - skyPercent),
                    new Color(App.GREEN.r * 0.8f, App.GREEN.g * 0.8f, App.GREEN.b * 0.8f, 1), true, 200, 80, App.GREEN);
            grass[i].setPosition(i * chunkSize, 0);
            sky[i] = new MapActor(chunkSize, getHeight() * skyPercent, App.SKY_BLUE_2, false, 0, 0, App.SKY_BLUE_1);
            sky[i].setPosition(i * chunkSize, grass[i].getHeight());
            addActor(grass[i]);
            addActor(sky[i]);
        }
        setZoom(maxUnzoom());
        setName("environement");
    }

    public void setZoom(float zoom) { setScale(zoom, zoom); }
    public boolean isWithGrass() { return withGrass; }
    public void setWithGrass(boolean withGrass) { this.withGrass = withGrass; }

    public float maxUnzoom() { return 1f / Math.min(getWidth() / Gdx.graphics.getWidth(), getHeight() / Gdx.graphics.getHeight()); }
    public float getVisibleWidth() { return camera.viewportWidth / getScaleX(); }
    public float getVisibleHeight() { return camera.viewportHeight / getScaleY(); }
    public float getMinVisibleX() { return camera.position.x / getScaleX() - getVisibleWidth() / 2; }
    public float getMaxVisibleX() { return camera.position.x / getScaleX() + getVisibleWidth() / 2; }
    public float getMinVisibleY() { return camera.position.y / getScaleY() - getVisibleHeight() / 2; }
    public float getMaxVisibleY() { return camera.position.y / getScaleY() + getVisibleHeight() / 2; }

    /**
     * {@summary Act for actors &#38; be sure that the background actors always cover all background pixels.}
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        placeBackgroundactorIfNeeded();
    }

    /**
     * {@summary Draw the background.}
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        float secToCycle = 3 * 60 + 54;
        chrono.updateDuree();
        float ligth = 1 - ((chrono.getDuree() % (secToCycle * 1000) / (secToCycle * 1000f))); // [0.5 ; 1]
        if (ligth < 0.5f) {
            ligth = 1f - ligth;
        }
        if (shapeDrawer == null) {
            shapeDrawer = Shapes.createShapeDrawer(batch);
        }
        shapeDrawer.setColor(0, 0, 0, 1f - ligth);
        shapeDrawer.filledRectangle(0, 0, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight());
    }

    /**
     * {@summary Be sure that the background actors always cover all background pixels.}
     */
    private void placeBackgroundactorIfNeeded() {
        // If sky[0] is out of the screen, move it to the right of sky[1] & swap them in array
        // Same for grass
        if (sky[0].getX() + sky[0].getWidth() < getMinVisibleX()) {
            App.log(0, "sky[0] is out of the screen, move it to the right of sky[1] & swap them in array");
            sky[0].setPosition(sky[1].getX() + sky[1].getWidth(), sky[0].getY());
            MapActor skyTmp = sky[0];
            sky[0] = sky[1];
            sky[1] = skyTmp;
            grass[0].setPosition(grass[1].getX() + grass[1].getWidth(), grass[0].getY());
            MapActor grassTmp = grass[0];
            grass[0] = grass[1];
            grass[1] = grassTmp;

            float offset = sky[0].getWidth();
            for (Actor actor : getChildren()) { // everyone is draw back.
                actor.setPosition(actor.getX() - offset, actor.getY());
            }
            camera.position.x = Gdx.graphics.getWidth() / 2f; // back to start point.
        }
    }

}

package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.usual.Chrono;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class EnvironmentMenuScreen extends Group {
    private MenuScreen menuScreen;
    private final Chrono chrono;
    private float skyPercent = 0.5f;
    private boolean withGrass;
    private MapActor grass;
    private MapActor sky;
    private OrthographicCamera camera;

    public EnvironmentMenuScreen(MenuScreen menuScreen, OrthographicCamera camera) {
        this.menuScreen = menuScreen;
        this.camera = camera;
        chrono = new Chrono();
        chrono.start();
        withGrass = true;
        setSize(10000, 4000);
        grass = new MapActor(getWidth(), getHeight() * (1 - skyPercent), App.GREEN, true, 200, 80);
        grass.setPosition(0, 0);
        sky = new MapActor(getWidth(), getHeight() * skyPercent, App.SKY_BLUE, false, 0, 0);
        sky.setPosition(0, grass.getHeight());
        addActor(grass);
        addActor(sky);
        setZoom(maxUnzoom());

        // App.log(2, "maxX : " + maxX());
        setName("environement");
    }

    public void setZoom(float zoom) { setScale(zoom, zoom); }
    public boolean isWithGrass() { return withGrass; }
    public void setWithGrass(boolean withGrass) { this.withGrass = withGrass; }

    public float maxUnzoom() { return 1f / Math.min(getWidth() / Gdx.graphics.getWidth(), getHeight() / Gdx.graphics.getHeight()); }
    // width - displayWidth
    // public float maxX() { return getWidth() - getWidth() / getScaleX(); }

    @Override
    public void act(float delta) {
        super.act(delta);
        // if (camera.position.x / getScaleX() > maxX()) { // not working well.
        // camera.position.x = 0;
        // }
        // camera.lookAt(0, 0, 0);
        // setPosition(getX() - delta * SPEED, getY());
        // App.log(2, "x : " + camera.position.x / getScaleX() + " maxX : " + maxX());
        // if (-getX() > maxX()) {
        // App.log(2, "loop");
        // setPosition(0, getY());
        // }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // setWithGrass(!menuScreen.getLevel().getLetter().equals("F"));
        grass.setVisible(isWithGrass());
        super.draw(batch, parentAlpha);


        // float secToCycle = 3 * 60 + 54;
        // chrono.updateDuree();
        // float ligth = 1 - ((chrono.getDuree() % (secToCycle * 1000) / (secToCycle * 1000f))); // [0.5 ; 1]
        // if (ligth < 0.5f) {
        // ligth = 1f - ligth;
        // }
        // Shapes.drawSky(getX(), getY() + getHeight() * (1 - skyPercent), getWidth(), getHeight() * skyPercent, ligth);
        // if (withGrass) {
        // Shapes.drawGrass(getX(), getY(), getWidth(), getHeight() * (1 - skyPercent), ligth);
        // } else {
        // Shapes.drawUnderground(getX(), getY(), getWidth(), getHeight() * (1 - skyPercent), 0.6f, 0.4f);
        // }
    }

}

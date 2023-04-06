package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.usual.Chrono;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class EnvironmentMenuScreen extends Group {
    private MenuScreen menuScreen;
    private final Chrono chrono;
    private float skyPercent = 0.5f;
    private boolean withGrass;
    private MapActor grass;
    private MapActor sky;
    private OrthographicCamera camera;
    private ShapeDrawer shapeDrawer;

    public EnvironmentMenuScreen(MenuScreen menuScreen, OrthographicCamera camera) {
        this.menuScreen = menuScreen;
        this.camera = camera;
        chrono = new Chrono();
        chrono.start();
        withGrass = true;
        setSize(10000, 4000);
        grass = new MapActor(getWidth(), getHeight() * (1 - skyPercent),
                new Color(App.GREEN.r * 0.8f, App.GREEN.g * 0.8f, App.GREEN.b * 0.8f, 1), true, 200, 80, App.GREEN);
        grass.setPosition(0, 0);
        sky = new MapActor(getWidth(), getHeight() * skyPercent, App.SKY_BLUE_2, false, 0, 0, App.SKY_BLUE_1);
        sky.setPosition(0, grass.getHeight());
        addActor(grass);
        addActor(sky);
        setZoom(maxUnzoom());

        setName("environement");
    }

    public void setZoom(float zoom) { setScale(zoom, zoom); }
    public boolean isWithGrass() { return withGrass; }
    public void setWithGrass(boolean withGrass) { this.withGrass = withGrass; }

    public float maxUnzoom() { return 1f / Math.min(getWidth() / Gdx.graphics.getWidth(), getHeight() / Gdx.graphics.getHeight()); }

    @Override
    public void act(float delta) { super.act(delta); }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // setWithGrass(!menuScreen.getLevel().getLetter().equals("F"));
        // grass.setVisible(isWithGrass());
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
        shapeDrawer.filledRectangle(getX(), getY(), getWidth(), getHeight());
    }

}

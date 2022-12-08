package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.MapItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class VideoScreen implements Screen {
    private final App game;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;

    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game the App where the Screen is display
     */
    public VideoScreen(final App game) {
        this.game = game;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(w * 0.5f, h * 0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        Texture t = new Texture(Gdx.files.internal("images/" + "videoBackground" + ".png"));
        Actor actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                batch.draw(new TextureRegion(t), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(),
                        getScaleY(), getRotation());
            }
        };
        actor.setSize((int) (w * 1.5), (int) (h * 1.5));
        stage.addActor(actor);
        // TODO form a groups off actor or load all images in one actor where we can control rotation of the wings.
        MapItem mi = new Ladybug();
        MapItemActor body = new MapItemActor("Creatures/ladybug side view body", mi);
        MapItemActor wing = new MapItemActor("Creatures/ladybug side view wing", mi);
        // MapItemActor body = new MapItemActor("Creatures/ladybug side view body", mi);
        body.setZoom(0.5f);
        wing.setZoom(0.5f);
        stage.addActor(body);
        stage.addActor(wing);

        App.log(0, "constructor", "new VideoScreen: " + toString());
    }

    @Override
    public void show() { // TODO Auto-generated method stub
    }

    /**
     * {@summary Draw all thing that need to be draw during a game.}
     * 
     * @see com.badlogic.gdx.Screen#render(float)
     */
    @Override
    public void render(float delta) {
        handleInput();
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
    }

    private void handleInput() {
        // while debuging game close on escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            game.dispose();
    }

    @Override
    public void resize(int width, int height) { // TODO Auto-generated method stub
    }

    @Override
    public void pause() { // TODO Auto-generated method stub
    }

    @Override
    public void resume() { // TODO Auto-generated method stub
    }

    @Override
    public void hide() { // TODO Auto-generated method stub
    }

    @Override
    public void dispose() { // TODO Auto-generated method stub
    }

}

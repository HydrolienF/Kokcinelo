package fr.formiko.kokcinelo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.formiko.kokcinelo.model.Creature;

/**
 * {*@summary The action game screen.}
 * 
 * @see com.badlogic.gdx.Screen
 */
public class GameScreen implements Screen {
    private final App game;
    private Viewport viewport;
    private Stage stage;

    static OrthographicCamera camera;
    private float rotationSpeed;
    private float maxZoom;
    private Controller controller;
    private int playerId;

    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game the App where the Screen is display
     */
    public GameScreen(final App game) {
        // TODO move to GameState.java the state of current game
        this.game = game;
        controller = new Controller();
        controller.createNewGame();
        playerId = 0;

        // Gdx.input.setCursorCatched(true);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        for (Creature c : controller.allCreatures()) {
            stage.addActor(c.getActor());
        }

        rotationSpeed = 0.5f;
        maxZoom = 0.2f;

        InputProcessor inputProcessor = (InputProcessor) new InputCore(this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    /**
     * @return the camera
     */
    public static Camera getCamera() {
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
    }

    /**
     * {@summary Draw all thing that need to be draw during a game.}
     * 
     * @see com.badlogic.gdx.Screen#render(float)
     */
    @Override
    public void render(float delta) {
        handleInput(); // Done before draw to avoid some GUI glitch
        ScreenUtils.clear(0.1f, 1f, 0f, 1);
        controller.updateActorVisibility(playerId);
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
    }

    /**
     * {@summary Handle user input &#38; mostly move camera.}<br>
     * Some input are handle on InputCore that allow more input handling.
     */
    private void handleInput() {
        // while debuging game close on escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            game.dispose();

        // TODO get a vector from mouse position & send it to controler to move the
        // player
        double moveY = 0;
        double moveX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveY += Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveY -= Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveX -= Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveX += Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            camera.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            camera.rotate(rotationSpeed, 0, 0, 1);
        }
        if (camera.zoom < maxZoom) {
            camera.zoom = maxZoom;
        }

        // TODO synchonise camera on creature insted of creature on camera.
        moveY *= 200;
        moveX *= 200;
        // if(moveX!=0)
        // System.out.println(camera.position);
        // tell the camera to update its matrices.
        camera.translate((int) moveX, (int) moveY, 0);
        controller.synchonisePlayerCreatureWithCamera(camera, playerId);
    }

    /**
     * {@summary Resize ViewPort when Screen is resize.}
     * 
     * @see com.badlogic.gdx.Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }
}
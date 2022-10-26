package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.view.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {*@summary The action game screen.}
 * 
 * @see com.badlogic.gdx.Screen
 */
public class GameScreen implements Screen {
    private final App game;
    private Viewport viewport;
    private Stage stage;
    private Hud hud;
    static OrthographicCamera camera;
    private float rotationSpeed;
    private float maxZoom;
    private Controller controller;
    private int playerId;
    // private Label playerScore;
    private boolean isPause;

    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game
     *            the App where the Screen is display
     */
    public GameScreen(final App game) {
        // TODO move to GameState.java the state of current game
        this.game = game;
        controller = new Controller(game, this);
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
        for (Actor a : controller.allActors()) {
            stage.addActor(a);
        }

        rotationSpeed = 0.5f;
        maxZoom = 0.2f;

        InputProcessor inputProcessor = (InputProcessor) new InputCore(this);
        Gdx.input.setInputProcessor(inputProcessor);
        createTextUI();
    }

    public Controller getController() {
        return controller;
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
        if (!isPause) {
            update(delta);
        }
        // ScreenUtils.clear(0.1f, 1f, 0f, 1);
        ScreenUtils.clear(0f, 0f, 0f, 1);
        // ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
        game.batch.setProjectionMatrix(camera.combined);
        controller.updateActorVisibility(playerId);
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if (!isPause) {
            if (isTimeUp()) {
                pause();
                // TODO show end screen menu.
            }
        }
    }

    /**
     * {@summary Handle user input &#38; mostly move camera.}<br>
     * Some input are handle on InputCore that allow more input handling.
     */
    private void handleInput() {
        // while debuging game close on escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            game.dispose();

        if (isPause) {
            return;
        }
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

        controller.movePlayer(playerId, moveX, moveY);
        controller.interact();
    }

    private void update(float delta) {
        hud.update(delta);
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
        isPause = true;

    }

    @Override
    public void resume() {
        isPause = false;

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    private void createTextUI() {
        // create our game HUD for scores/timers/level info
        hud = new Hud(game.batch, 60);
        // Label.LabelStyle labelStyle = new Label.LabelStyle();
        // labelStyle.font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        // labelStyle.fontColor = Color.RED;
        // playerScore = new Label("", labelStyle);
        // playerScore.setSize(Gdx.graphics.getWidth(), 30);
        // playerScore.setPosition(0, 0);
        // // label.setAlignment(Align.center);
        // stage.addActor(playerScore);
    }

    public void setPlayerScore(int score) {
        // playerScore.setText(""+score);
        hud.setPlayerScore(score);
    }

    public boolean isTimeUp() {
        return hud.isTimeUp();
    }
}
package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.InputCore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@summary The action game screen.}
 * 
 * @see com.badlogic.gdx.Screen
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class GameScreen implements Screen {
    private final App game;
    private Viewport viewport;
    private Stage stage;
    private Hud hud;
    private EndGameMenu egm;
    static OrthographicCamera camera;
    private float rotationSpeed;
    private float maxZoom;
    private Controller controller;
    private int playerId;
    // private Label playerScore;
    private boolean isPause;
    private boolean isStop;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game the App where the Screen is display
     */
    public GameScreen(final App game) {
        this.game = game;
        controller = new Controller(game, this);
        controller.createNewGame();
        playerId = 0;

        // Gdx.input.setCursorCatched(true);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(w * 0.5f, h * 0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        for (Actor a : controller.allActors()) {
            stage.addActor(a);
        }

        rotationSpeed = 0.5f;
        maxZoom = 0.2f;

        InputProcessor inputProcessor = (InputProcessor) new InputCore(this, controller);
        Gdx.input.setInputProcessor(inputProcessor);
        createTextUI();
        game.playGameMusic();
    }

    // GET SET -------------------------------------------------------------------
    public Controller getController() { return controller; }
    public boolean isPause() { return isPause; }
    public boolean isStop() { return isStop; }
    public static OrthographicCamera getCamera() { return camera; }
    public Stage getStage() { return stage; }


    // FUNCTIONS -----------------------------------------------------------------
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
        getController().updateActorVisibility(playerId);
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        if (!isPause) {
            if (isTimeUp() || getController().isAllAphidGone()) {
                controller.gameOver();
            }
        }
        if (egm != null) {
            game.batch.setProjectionMatrix(egm.getStage().getCamera().combined);
            egm.getStage().draw();
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
        // double moveY = 0;
        // double moveX = 0;
        // if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        // moveY += Gdx.graphics.getDeltaTime();
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        // moveY -= Gdx.graphics.getDeltaTime();
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        // moveX -= Gdx.graphics.getDeltaTime();
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        // moveX += Gdx.graphics.getDeltaTime();
        // }
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            camera.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            camera.rotate(rotationSpeed, 0, 0, 1);
        }
        if (camera.zoom < maxZoom) {
            camera.zoom = maxZoom;
        }
        controller.movePlayer(playerId);
        controller.moveAphids();
        // controller.movePlayer(playerId, moveX, moveY);
        controller.interact();
    }
    /***
     * {@summary Update all subpanels.}
     * 
     * @param delta time since last update
     */
    private void update(float delta) { hud.update(delta); }

    /***
     * {@summary Resize ViewPort when Screen is resize.}
     * 
     * @see com.badlogic.gdx.Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) { viewport.update(width, height); }

    @Override
    public void pause() { isPause = true; }
    @Override
    public void resume() {
        if (isStop) {
            System.out.println("Can't resume stop game.");
            return;
        }
        isPause = false;
    }

    public void stop() {
        isStop = true;
        pause();
    }

    // create our game HUD for scores/timers/level info
    private void createTextUI() { hud = new Hud(game.batch, 60); }
    public void createEndGameMenu(int score, int maxScore, boolean haveWin) { egm = new EndGameMenu(game.batch, score, maxScore, haveWin); }
    public void setPlayerScore(int score) { hud.setPlayerScore(score); }
    public boolean isTimeUp() { return hud.isTimeUp(); }


    // TODO Auto-generated method stub
    @Override
    public void hide() {}
    @Override
    public void dispose() {}
    @Override
    public void show() {}
}
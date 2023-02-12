package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.InputCore;
import fr.formiko.kokcinelo.tools.Musics;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
 * @version 0.2
 * @since 0.1
 */
public class GameScreen implements Screen {
    private final App game;
    private Viewport viewport;
    private Stage stage;
    private Hud hud;
    private EndGameMenu egm;
    private EscapeGameMenu em;
    static OrthographicCamera camera;
    // private float rotationSpeed;
    private float maxZoom;
    private boolean isPause;
    private boolean isStop;
    private boolean stopAfterNextDrawBool;
    private InputMultiplexer inputMultiplexer;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game the App where the Screen is display
     */
    public GameScreen(final App game) {
        this.game = game;

        // Gdx.input.setCursorCatched(true);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(w * 0.5f, h * 0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        for (Actor a : Controller.getController().allActors()) {
            stage.addActor(a);
        }

        // rotationSpeed = 0.5f;
        maxZoom = 0.2f;

        InputProcessor inputProcessor = (InputProcessor) new InputCore(this);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
        createTextUI();
        pause();
        App.log(0, "constructor", "new GameScreen: " + toString());
    }

    // GET SET -------------------------------------------------------------------
    public Controller getController() { return Controller.getController(); }
    public boolean isPause() { return isPause; }
    public boolean isStop() { return isStop; }
    public static OrthographicCamera getCamera() { return camera; }
    public Stage getStage() { return stage; }
    public void addProcessor(InputProcessor ip) { inputMultiplexer.addProcessor(ip); }
    public void removeProcessor(InputProcessor ip) { inputMultiplexer.removeProcessor(ip); }


    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Draw all thing that need to be draw during a game.}
     * 
     * @see com.badlogic.gdx.Screen#render(float)
     */
    @Override
    public void render(float delta) {
        // App.log(1, "isPause: " + isPause);
        boolean stopAtTheEnd = stopAfterNextDrawBool;
        handleInput(); // Done before draw to avoid some GUI glitch
        if (!isPause) {
            if (delta < 0.5) {
                update(delta);
            } else {
                App.log(1, "skip a delta to avoid lag.");
            }
        }
        ScreenUtils.clear(0f, 0f, 0f, 1);
        game.batch.setProjectionMatrix(camera.combined);

        getController().updateActorVisibility(Controller.getController().getLocalPlayerId());
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        if (!isPause) {
            if (isTimeUp() || getController().isAllAphidGone()) {
                if (!stopAtTheEnd) {
                    getController().gameOver();
                }
            }
            if (getController().isAllLadybugGone()) {
                if (!stopAtTheEnd) {
                    getController().addScore((int) hud.getGameTime());
                    getController().gameOver();
                }
            }
        }

        if (egm != null) {
            game.batch.setProjectionMatrix(egm.getStage().getCamera().combined);
            egm.getStage().act(delta);
            egm.getStage().draw();
        }
        // if (isPause && em == null) {
        // createEscapeMenu();
        // }
        if (em != null) {
            game.batch.setProjectionMatrix(em.getStage().getCamera().combined);
            em.getStage().act(delta);
            em.getStage().draw();
        }
        if (stopAtTheEnd) {
            stopAfterNextDrawBool = false;
            stop();
        }
    }

    /**
     * {@summary Handle user input &#38; mostly move camera.}<br>
     * Some input are handle on InputCore that allow more input handling as user click on the screen.
     */
    private void handleInput() {
        // // while debuging game close on escape
        // if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
        // game.dispose();
        // }
        if (Gdx.input.isKeyPressed(Input.Keys.F11)) {
            Boolean fullScreen = Gdx.graphics.isFullscreen();
            Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
            if (fullScreen == true)
                Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
            else
                Gdx.graphics.setFullscreenMode(currentMode);
        }

        if (isPause) {
            return;
        }
        if (camera.zoom < maxZoom) {
            camera.zoom = maxZoom;
        }
        getController().playAFrame();
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
    public void resize(int width, int height) {
        if (width != 0 && height != 0) { // To avoid an issue on Windows were resize(0,0) is call on tab.
            viewport.update(width, height);
        }
    }
    /***
     * {@summary Temporary pause current game.}
     */
    @Override
    public void pause() {
        isPause = true;
        Musics.pause();
    }
    /**
     * {@summary Resume current Game.}
     * Current game, may not be resumable if it have been stop.
     */
    @Override
    public void resume() {
        if (isStop) {
            App.log(2, "Can't resume stop game.");
            return;
        }
        if (em != null) {
            App.log(2, "Can't resume escaped game.");
            return;
        }
        isPause = false;
        Musics.resume();
    }
    /**
     * {@summary Definitivly stop current game.}
     */
    public void stop() {
        isStop = true;
        if (!isPause) {
            pause();
        }
    }
    /***
     * {@summary Definitivly stop current game after next draw.}
     * It can be use to update game screen a last time before stop.
     */
    public void stopAfterNextDraw() { stopAfterNextDrawBool = true; }

    // create our game HUD for scores/timers/level info
    private void createTextUI() { hud = new Hud(game.batch); }
    /**
     * {@summary Create an EndGameMenu as a HUD &#38; add the input listener to this.}
     */
    public void createEndGameMenu(int score, int maxScore, boolean haveWin) {
        egm = new EndGameMenu(game.batch, score, maxScore, haveWin);
        addProcessor(egm.getStage());
    }
    /**
     * {@summary Create a simple escape menu.}
     */
    public void createEscapeMenu() {
        em = new EscapeGameMenu(game.batch);
        addProcessor(em.getStage());
    }
    /**
     * {@summary Remove the escape menu.}
     */
    public void removeEscapeMenu() {
        if (em != null) {
            removeProcessor(em.getStage());
            em.dispose();
            em = null;
        }
    }
    public void setPlayerScore(int score) { hud.setPlayerScore(score); }
    public boolean isTimeUp() { return hud.isTimeUp(); }
    public void setGameTime(int gameTime) { hud.setGameTime(gameTime); }

    /**
     * {@summary Dispose the screen.}
     * It stop music and free memory.
     */
    @Override
    public void dispose() {
        App.log(0, "destructor", "dispose GameScreen: " + toString());
        stage.dispose();
        Musics.stop();
    }


    @Override
    public void hide() {}
    @Override
    public void show() {}
}
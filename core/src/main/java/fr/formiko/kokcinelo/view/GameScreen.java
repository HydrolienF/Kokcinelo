package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.InputCore;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.tools.KScreen;
import fr.formiko.kokcinelo.tools.Musics;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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
 * @version 1.3
 * @since 0.1
 */
public class GameScreen extends KScreen implements Screen {
    private final App game;
    private Viewport viewport;
    private Stage backgroundStage;
    private Stage foregroundStage;
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
    // private Texture mask;
    /** Used to store reusable frame buffer */
    private Map<Integer, FrameBuffer> frameBuffers;
    /** Used to store reusable texture */
    private Map<Integer, Texture> visibleCircleTextures;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game the App where the Screen is display
     */
    public GameScreen(final App game) {
        super();
        this.game = game;
        frameBuffers = new HashMap<>();
        visibleCircleTextures = new HashMap<>();

        // Gdx.input.setCursorCatched(true);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(w * 0.5f, h * 0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        backgroundStage = new Stage(viewport);
        foregroundStage = new Stage(viewport);
        for (Actor a : Controller.getController().allActors()) {
            String name = a.getName() == null ? "" : a.getName();
            switch (name) {
                case "background":
                    backgroundStage.addActor(a);
                    break;
                case "foreground":
                    foregroundStage.addActor(a);
                    break;
                default:
                    stage.addActor(a);
                    break;
            }
        }
        stage.setDebugAll(Controller.isDebug());
        backgroundStage.setDebugAll(Controller.isDebug());
        foregroundStage.setDebugAll(Controller.isDebug());

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
        long time = System.currentTimeMillis();
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
        if (!isPause) {
            stage.act(delta);// update actions are drawn here
            backgroundStage.act(delta);
            foregroundStage.act(delta);
        }

        backgroundStage.draw();

        if (Controller.getController().isSpectatorMode()) {
            // stage.draw();
            game.batch.begin();
            stage.getCamera().update();
            game.batch.setProjectionMatrix(stage.getCamera().combined);
            stage.getRoot().draw(game.batch, 1);
            game.batch.end();
        } else { // draw actors only in the visible circles.
            drawVisibleMapItem(delta);
        }

        foregroundStage.draw();


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
                    // if player is an ant or an aphid give it bonus score for time.
                    if (!(getController().getPlayerCreature() instanceof Ladybug)) {
                        getController().addScore((int) hud.getGameTime());
                    }
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
            Controller.getController().playEndGameSound();
        }
        times.add((int) (System.currentTimeMillis() - time));
    }

    /**
     * {@summary Draw all visible map item.}<br>
     * Visible map item are all item that are in player creature vision range or friendly creature vision range.
     * 
     * @param delta time since last draw
     */
    private void drawVisibleMapItem(float delta) {
        for (Creature ligthSource : getController().getLightSources()) {
            // FrameBuffer frameBuffer = getFrameBuffers(2000);
            // FrameBuffer frameBuffer = getFrameBuffers((int) ligthSource.getVisionRadius());
            // frameBuffer.bind();
            game.batch.begin();
            stage.getCamera().update();
            game.batch.setProjectionMatrix(stage.getCamera().combined);
            drawVisibleAreaCreature(ligthSource);
            drawVisibleAreaMask(ligthSource);
            game.batch.end();
            // frameBuffer.end();

            // // draw frame buffer into screen.
            // Texture texture = frameBuffer.getColorBufferTexture();
            // Sprite sprite = new Sprite(texture);
            // sprite.flip(false, true);
            // // sprite.setScale(camera.zoom);
            // // sprite.setPosition(Controller.getController().getGameState().getMapActor().getX(),
            // // Controller.getController().getGameState().getMapActor().getY());
            // sprite.setPosition(0, 0);
            // // sprite.setCenter(ligthSource.getCenterX() * camera.zoom, ligthSource.getCenterY() * camera.zoom);
            // // sprite.setScale(Map);
            // game.batch.begin();
            // // game.batch.setProjectionMatrix(camera.combined);
            // sprite.draw(game.batch);
            // game.batch.end();
        }
    }

    /**
     * Draw all creatures in the visible area.
     */
    private void drawVisibleAreaCreature(Creature ligthSource) {
        game.batch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        // ScreenUtils.clear(Color.CLEAR);
        // TODO Draw only if in visible area may improve performance
        // stage.getCamera().update();
        stage.getRoot().draw(game.batch, 1);
    }

    /**
     * Draw a mask over the visible area.
     * Mask will remove some previously drawn pixels depending of visible circle texture.
     * Colored part of the mask will be removed (with same alpha).
     */
    private void drawVisibleAreaMask(Creature ligthSource) {
        game.batch.flush(); // flush need to be call before changing blend function
        game.batch.setBlendFunctionSeparate(GL30.GL_ZERO, GL30.GL_ONE, GL30.GL_ZERO, GL30.GL_ONE_MINUS_SRC_ALPHA);
        // game.batch.draw(getVisibleCircleTexture((int) ligthSource.getVisionRadius()), 0, 0);

        game.batch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
    }


    private FrameBuffer getFrameBuffers(int radius) {
        if (!frameBuffers.containsKey(radius)) {
            frameBuffers.put(radius, new FrameBuffer(Format.RGBA8888, radius * 2, radius * 2, false));
        }
        return frameBuffers.get(radius);
    }
    public Texture getVisibleCircleTexture(int radius) {
        if (!visibleCircleTextures.containsKey(radius)) {
            // visibleCircleTextures.put(radius, createVisibleCircleTexture(radius));
        }
        return visibleCircleTextures.get(radius);
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
        if (!needResize(width, height))
            return;
        viewport.update(width, height);
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
        super.displayPerf();
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
    public void hide() {
        // Nothing to do when hiding the screen.
    }
    @Override
    public void show() {
        // Nothing to do when showing the screen.
    }
}

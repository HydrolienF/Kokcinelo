package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.view.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * {@summary Controller is the link between View &#38; model.}
 * Because of Seen2D Actor, there is some view item in the model.
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class Controller {
    private GameState gs;
    private App app;
    private GameScreen gScreen;
    private boolean spectatorMode;

    private static Controller controller;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     * 
     * @param app app to send action to
     */
    public Controller(App app) {
        this.app = app;
        controller = this;
        App.log(0, "constructor", "new Controller: " + toString());
    }

    // GET SET -------------------------------------------------------------------
    public static Controller getController() { return controller; }
    public static void setController(Controller controller) { Controller.controller = controller; }
    public GameScreen getGameScreen() { return gScreen; }
    public int getLocalPlayerId() { return gs.getLocalPlayerId(); }
    public boolean isSpectatorMode() { return spectatorMode; }
    public void setSpectatorMode(boolean spectatorMode) { this.spectatorMode = spectatorMode; }

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Update zoom of camera.}
     * 
     * @param amountY zoom amount
     */
    public void addZoom(float amountY) {
        amountY *= -1;
        // getCamera().zoom += amountY * 0.05f;
        getCamera().zoom *= (1 - amountY / 20);
        if (getCamera().zoom < 0.1f) {
            getCamera().zoom = 0.1f;
        }
    }

    /**
     * {@summary Move the player Creature &#38; synchonize Camera and masks.}
     * 
     * @param playerId id of the player to move
     */
    public void movePlayer(int playerId) {
        Creature c = gs.getPlayerCreature(playerId);
        c.moveFront();
        c.moveIn(gs.getMapWidth(), gs.getMapHeight());
        // synchonize things that depend of c position
        synchronizeCamera(c);

        if (gs.getMapActorFg() != null) {
            gs.getMapActorFg().setX(c.getCenterX() - gs.getMapActorFg().getWidth() / 2);
            gs.getMapActorFg().setY(c.getCenterY() - gs.getMapActorFg().getHeight() / 2);
            gs.getMapActorFg().setVisible(!isSpectatorMode());
        }
        c.goTo(getVectorStageCoordinates(Gdx.input.getX(), Gdx.input.getY()));
    }

    /**
     * {@summary Move aphids.}
     * Aphids first run away from the closest ladybug they can see if they can see one.
     * Else they move slowly to a random direction &#38; some time change it.
     * If they hit a wall, they change there wanted rotation angle for the nexts turns.
     */
    public void moveAphids() {
        for (Aphid aphid : gs.getAphids()) {
            Ladybug ladybug = aphid.closestLadybug(gs.getLadybugs());
            if (ladybug != null) {
                // Run away move
                aphid.runAwayFrom(new Vector2(ladybug.getCenterX(), ladybug.getCenterY()));
                aphid.moveFront();
            } else {
                // Normal move
                double r = Math.random() / (Gdx.graphics.getDeltaTime() * 100);
                if (r < 0.02) { // randomize rotation
                    aphid.setWantedRotation((float) (Math.random() * 40) - 20f);
                }
                aphid.moveFront(0.3f);
            }
            // if have been move to avoid wall
            if (aphid.moveIn(gs.getMapWidth(), gs.getMapHeight())) {
                if (aphid.getWantedRotation() == 0f) { // if have not already choose a new angle to get out.
                    aphid.setWantedRotation((160f + (float) (Math.random() * 40)) % 360f);
                }
            }
        }
    }

    /**
     * {@summary Synchronize the camera with the given Creature.}
     * 
     * @param c creature to synchronize with
     */
    public void synchronizeCamera(Creature c) {
        getCamera().position.x = c.getCenterX();
        getCamera().position.y = c.getCenterY();
    }
    /**
     * {@summary Create a new Game.}
     * Create the GameState with Game data.
     * Set current Screen as a new GameScreen.
     */
    public void createNewGame() {
        int gameTime = 60;
        setSpectatorMode(false);
        app.createGameMusic();
        gs = GameState.builder().setMaxScore(100).setMapHeight(2000).setMapWidth(2000).build();
        gScreen = new GameScreen(app);
        app.setScreen(gScreen);
        app.getGameMusic().play();
        App.log(1, "start new Game");
        // app.getGameMusic().setPosition(178.1f - gameTime); // end at 178
        gScreen.resume();
        gScreen.setGameTime(gameTime);
        App.log(1, "new Game started");
    }
    public void restartGame() { createNewGame(); }
    public void updateActorVisibility(int playerId) { gs.updateActorVisibility(playerId, spectatorMode); }
    public Iterable<Creature> allCreatures() { return gs.allCreatures(); }
    public Iterable<Actor> allActors() { return gs.allActors(); }
    public boolean isAllAphidGone() { return gs.isAllAphidGone(); }
    // public void removeActorFromStage(Actor actor) { actor.remove(); }

    /**
     * {@summary Let Creature interact with each other.}
     * It let ladybugs eat aphids and if they do, update player score &#38; play matching sound.
     */
    public void interact() {
        if (gs.ladybugEat()) {
            gScreen.setPlayerScore(gs.getPlayer(getLocalPlayerId()).getScore());
            app.playEatingSound();
        }
    }
    /**
     * {@summary End game by launching sound &#38; end game menu.}
     */
    public void gameOver() {
        App.log(1, "gameOver");
        if (gScreen.isStop()) {
            return;
        }
        app.getGameMusic().dispose();
        setSpectatorMode(true);
        gScreen.stopAfterNextDraw();
        boolean haveWin = gs.getPlayer(getLocalPlayerId()).getScore() >= gs.getMaxScore() / 2;
        app.playEndGameSound(haveWin);
        gScreen.createEndGameMenu(gs.getPlayer(getLocalPlayerId()).getScore(), gs.getMaxScore(), haveWin);
    }
    /**
     * {@summary Pause game or resume depening of current state.}
     * It pause move of creature & music.
     */
    public void pauseResume() {
        if (gScreen.isPause()) {
            gScreen.resume();
            app.getGameMusic().play();
        } else {
            gScreen.pause();
            app.getGameMusic().pause();
        }
    }

    public void dispose() { app.dispose(); }
    /**
     * {@summary Return current used camera.}
     * 
     * @return current used camera
     */
    private OrthographicCamera getCamera() {
        // return gScreen.camera;
        return GameScreen.getCamera();
    }

    private Vector2 getVectorStageCoordinates(float x, float y) { return gScreen.getStage().screenToStageCoordinates(new Vector2(x, y)); }
}

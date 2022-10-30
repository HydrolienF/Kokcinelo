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
    private static int playerId = 0;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     * 
     * @param app     app to send action to
     * @param gScreen GameScreen to send action to
     */
    public Controller(App app, GameScreen gScreen) {
        this.app = app;
        this.gScreen = gScreen;
    }

    // GET SET -------------------------------------------------------------------

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
        c.getActor().moveIn(gs.getMapWidth(), gs.getMapHeight());
        // synchonize things that depend of c position
        synchronizeCamera(c);
        if (gs.getMapActorFg() != null) {
            gs.getMapActorFg().setX(c.getActor().getCenterX() - gs.getMapActorFg().getWidth() / 2);
            gs.getMapActorFg().setY(c.getActor().getCenterY() - gs.getMapActorFg().getHeight() / 2);
        }
        c.goTo(getVectorStageCoordinates(Gdx.input.getX(), Gdx.input.getY()));
    }

    /**
     * {@summary Move aphids.}
     * Aphids first run away from the closest ladybug they can see if they can see one.
     * Else they move slowly to a random direction & some time change it.
     * If they hit a wall, they change there wanted rotation angle for the nexts turns.
     */
    public void moveAphids() {
        for (Aphid aphid : gs.getAphids()) {
            Ladybug ladybug = aphid.closestLadybug(gs.getLadybugs());
            if (ladybug != null) {
                aphid.runAwayFrom(new Vector2(ladybug.getActor().getCenterX(), ladybug.getActor().getCenterY()));
                aphid.moveFront();
            } else {
                double r = Math.random() / (Gdx.graphics.getDeltaTime() * 100);
                if (r < 0.01) {
                    aphid.getActor().setRotation((float) (aphid.getActor().getRotation()
                            + aphid.getMaxRotationPerSecond() * Gdx.graphics.getDeltaTime() * 2 * (Math.random() - 0.5)));
                }
                // TODO aphid need to stop rotate even if we don't set there rotation.
                // if (aphid.isRunningAway()) {
                // aphid.setWantedRotation(-1000f);
                // aphid.setRunningAway(false);
                // }
                aphid.moveFront(0.3f);
            }
            // if have been move to avoid wall
            if (aphid.getActor().moveIn(gs.getMapWidth(), gs.getMapHeight())) {
                aphid.setWantedRotation((aphid.getActor().getRotation() + 160f + (float) (Math.random() * 40)) % 360f);
            }
        }
    }

    /**
     * {@summary Synchronize the camera with the given Creature.}
     * 
     * @param c creature to synchronize with
     */
    public void synchronizeCamera(Creature c) {
        getCamera().position.x = c.getActor().getCenterX();
        getCamera().position.y = c.getActor().getCenterY();
    }

    public void createNewGame() { gs = GameState.builder().setMaxScore(2).setMapHeight(2000).setMapWidth(2000).build(); }
    public void updateActorVisibility(int playerId) { gs.updateActorVisibility(playerId); }
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
            gScreen.setPlayerScore(gs.getPlayer(0).getScore());
            app.playEatingSound();
        }
    }
    /**
     * {@summary End game by launching sound &#38; end game menu.}
     */
    public void gameOver() {
        boolean haveWin = gs.getPlayer(playerId).getScore() == gs.getMaxScore();
        app.playEndGameSound(haveWin);
        gScreen.createEndGameMenu(gs.getPlayer(playerId).getScore(), gs.getMaxScore(), haveWin);
    }
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

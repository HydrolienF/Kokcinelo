package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;
import fr.formiko.kokcinelo.view.GameScreen;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * {@summary Controller is the link between View &#38; model.}
 * Because of Seen2D Actor, there is some view item in the model.
 */
public class Controller {
    private GameState gs;
    private static Random ran;
    private App app;
    private GameScreen gScreen;
    private static int playerId = 0;

    public Controller(App app, GameScreen gScreen) {
        this.app = app;
        this.gScreen = gScreen;
    }

    public void addZoom(float amountY) {
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
        float moveAviable = 5;

        Creature c = gs.getPlayerCreature(playerId);
        c.getActor().moveFront(moveAviable);
        c.getActor().moveIn(gs.getMapWidth(), gs.getMapHeight());
        // synchonize things that depend of c position
        synchronizeCamera(c);
        if (gs.getMapActorFg() != null) {
            gs.getMapActorFg().setX(c.getActor().getCenterX() - gs.getMapActorFg().getWidth() / 2);
            gs.getMapActorFg().setY(c.getActor().getCenterY() - gs.getMapActorFg().getHeight() / 2);
        }
        mouseMoved(Gdx.input.getX(), Gdx.input.getY());
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

    public void createNewGame() { gs = GameState.builder().setMaxScore(100).setMapHeight(2000).setMapWidth(2000).build(); }
    public void updateActorVisibility(int playerId) { gs.updateActorVisibility(playerId); }
    public Iterable<Creature> allCreatures() { return gs.allCreatures(); }
    public Iterable<Actor> allActors() { return gs.allActors(); }
    public boolean isAllAphidGone() { return gs.isAllAphidGone(); }
    // public void removeActorFromStage(Actor actor) { actor.remove(); }

    public void interact() {
        if (gs.ladybugEat()) {
            gScreen.setPlayerScore(gs.getPlayer(0).getScore());
            app.playEatingSound();
        }
    }

    public void gameOver() {
        boolean haveWin = gs.getPlayer(playerId).getScore() == gs.getMaxScore();
        app.playEndGameSound(haveWin);
        gScreen.createEndGameMenu(gs.getPlayer(playerId).getScore(), gs.getMaxScore(), haveWin);
    }

    public void mouseMoved(int x, int y) {
        if (!gScreen.isPause()) {
            Vector2 v = gScreen.getStage().screenToStageCoordinates(new Vector2(x, y));
            Creature c = gs.getPlayerCreature(playerId);
            Vector2 v2 = new Vector2(v.x - c.getActor().getCenterX(), v.y - c.getActor().getCenterY());
            float previousRotation = c.getActor().getRotation() % 360;
            float newRotation = v2.angleDeg() - 90;
            float wantedRotation = (previousRotation - newRotation + 360) % 360;
            if (wantedRotation > 180) {
                wantedRotation -= 360;
            }
            float allowedRotation = Math.min(c.getMaxRotationPerSecond() * Gdx.graphics.getDeltaTime(), Math.abs(wantedRotation));
            if (wantedRotation > 0) {
                allowedRotation *= -1;
            }
            // System.out.println(wantedRotation + " " + allowedRotation);
            c.getActor().setRotation(previousRotation + allowedRotation);
        }
    }

    public static Random getRandom() {
        if (ran == null) {
            ran = new Random();
        }
        return ran;
    }

    private OrthographicCamera getCamera() {
        // return gScreen.camera;
        return GameScreen.getCamera();
    }
}

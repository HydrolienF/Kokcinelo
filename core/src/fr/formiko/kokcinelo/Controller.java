package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;
import fr.formiko.kokcinelo.view.GameScreen;
import java.util.Random;
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
        getCamera().zoom += amountY * 0.05f;
        // gScreen.replaceMaskBy(new
        // Circle(getCamera().position.x,getCamera().position.y,300*getCamera().zoom));
    }

    public void movePlayer(int playerId, double moveX, double moveY) {
        double moveAviable = 300;
        if (moveX != 0 && moveY != 0) {
            double multDiagonal = Math.sqrt(2) / 2.0;
            moveX *= moveAviable * multDiagonal;
            moveY *= moveAviable * multDiagonal;
        } else if (moveX != 0) {
            moveX *= moveAviable;

        } else if (moveY != 0) {
            moveY *= moveAviable;
        }

        Creature c = gs.getPlayerCreature(playerId);
        c.getActor().translate((float) moveX, (float) moveY);
        c.getActor().moveIn(gs.getMapWidth(), gs.getMapHeight());
        synchronizeCamera(c);
        if (gs.getMapActorFg() != null) {
            gs.getMapActorFg().setX(c.getActor().getCenterX() - gs.getMapActorFg().getWidth() / 2);
            gs.getMapActorFg().setY(c.getActor().getCenterY() - gs.getMapActorFg().getHeight() / 2);
        }
    }

    public void synchronizeCamera(Creature c) {
        getCamera().position.x = c.getActor().getCenterX();
        getCamera().position.y = c.getActor().getCenterY();
    }

    public void createNewGame() { gs = GameState.builder().setMaxScore(100).setMapHeight(2000).setMapWidth(2000).build(); }

    public void updateActorVisibility(int playerId) { gs.updateActorVisibility(playerId); }

    public Iterable<Creature> allCreatures() { return gs.allCreatures(); }

    public Iterable<Actor> allActors() { return gs.allActors(); }

    // public void removeActorFromStage(Actor actor){
    // actor.remove();
    // }

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

    public boolean isAllAphidGone() { return gs.isAllAphidGone(); }

    public void mouseMoved(int x, int y) {
        Vector2 v = gScreen.getStage().screenToStageCoordinates(new Vector2(x, y));
        Creature c = gs.getPlayerCreature(playerId);
        Vector2 v2 = new Vector2(v.x - c.getActor().getCenterX(), v.y - c.getActor().getCenterY());
        c.getActor().setRotation(v2.angleDeg() - 90);
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

package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;
import java.util.Random;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Controller {
    private GameState gs;
    private static Random ran;
    private App app;
    private GameScreen gScreen;

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

    public void createNewGame() {
        gs = GameState.builder()
                .setMapHeight(2000)
                .setMapWidth(2000)
                .build();
    }

    public void updateActorVisibility(int playerId) {
        gs.updateActorVisibility(playerId);
    }

    public Iterable<Creature> allCreatures() {
        return gs.allCreatures();
    }

    public Iterable<Actor> allActors() {
        return gs.allActors();
    }

    // public void removeActorFromStage(Actor actor){
    // actor.remove();
    // }

    public void interact() {
        if (gs.interact()) {
            gScreen.setPlayerScore(gs.getPlayer(0).getScore());
        }
    }

    public static Random getRandom() {
        if (ran == null) {
            ran = new Random();
        }
        return ran;
    }

    private OrthographicCamera getCamera() {
        return gScreen.camera;
    }
}

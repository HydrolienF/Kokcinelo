package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Controller {
    private GameState gs;
    private static Random ran;

    public Controller() {

    }

    public void addZoom(float amountY){
        //TODO view update zoom
        // screen.camera.zoom += amountY * 0.05f;
    }

    // TODO move the player camera & Creature
    public void movePlayer(int playerId) {

    }

    public void createNewGame() {
        gs = GameState.builder()
                .setMapHeight(1000)
                .setMapWidth(2000)
                .build();
    }

    public void updateActorVisibility(int playerId) {
        gs.updateActorVisibility(playerId);
    }

    public Iterable<Creature> allCreatures() {
        return gs.allCreatures();
    }
    public Iterable<Actor> allActors(){
        return gs.allActors();
    }

    public void synchonisePlayerCreatureWithCamera(OrthographicCamera camera, int playerId) {
        Creature c = gs.getPlayerCreature(playerId);
        c.getActor().setX(camera.position.x - c.getActor().getWidth() / 2);
        c.getActor().setY(camera.position.y - c.getActor().getHeight() / 2);
        if(gs.getMapActorFg()!=null){
            gs.getMapActorFg().clearToExclude();
            gs.getMapActorFg().addToExclude(c.getActor().getX(), c.getActor().getY(), c.getVisionRadius());
        }
    }

    public static Random getRandom() {
        if (ran == null) {
            ran = new Random();
        }
        return ran;
    }
}

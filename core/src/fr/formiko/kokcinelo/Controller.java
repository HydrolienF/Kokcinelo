package fr.formiko.kokcinelo;

import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.graphics.OrthographicCamera;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;

public class Controller {
    private GameState gs;
    private static Random ran;

    public Controller() {

    }

    // TODO move the player camera & Creature
    public void movePlayer(int playerId) {

    }

    public void createNewGame() {
        gs = GameState.builder()
                .setMapHeight(1000)
                .setMapWidth(1000)
                .build();
    }

    public void updateActorVisibility(int playerId) {
        gs.updateActorVisibility(playerId);
    }

    public Iterable<Creature> allCreatures() {
        return gs.allCreatures();
    }

    public void synchonisePlayerCreatureWithCamera(OrthographicCamera camera, int playerId) {
        Creature c = gs.getPlayerCreature(playerId);
        c.getActor().setX(camera.position.x - c.getActor().getWidth() / 2);
        c.getActor().setY(camera.position.y - c.getActor().getHeight() / 2);
    }

    public static Random getRandom() {
        if (ran == null) {
            ran = new Random();
        }
        return ran;
    }
}

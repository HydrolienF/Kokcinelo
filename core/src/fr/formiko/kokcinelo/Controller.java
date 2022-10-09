package fr.formiko.kokcinelo;

import java.util.Set;

import com.badlogic.gdx.graphics.OrthographicCamera;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;

public class Controller {
    private GameState gs;

    public Controller() {

    }

    // TODO move the player camera & Creature
    public void movePlayer(int playerId) {

    }

    public void createNewGame() {
        gs = GameState.builder()
                .setMapHeight(100)
                .setMapWidth(100)
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
}

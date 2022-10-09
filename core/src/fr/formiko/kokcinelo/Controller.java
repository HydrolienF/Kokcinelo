package fr.formiko.kokcinelo;

import java.util.Set;

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

    public Set<Creature> getCreatureToPrint(int playerId) {
        return gs.getCreatureToPrint(playerId);
    }
}

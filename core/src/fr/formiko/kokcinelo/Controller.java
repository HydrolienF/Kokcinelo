package fr.formiko.kokcinelo;

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

    // public Set<
}

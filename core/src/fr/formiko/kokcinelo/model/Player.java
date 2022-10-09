package fr.formiko.kokcinelo.model;

public class Player {
    /** Unique id for every Player */
    private final int id;
    private static int idCpt = 0;
    private Creature playedCreature;

    public Creature getPlayedCreature() {
        return playedCreature;
    }

    public void setPlayedCreature(Creature playedCreature) {
        this.playedCreature = playedCreature;
    }

    public Player(Creature c) {
        id = idCpt++;
        playedCreature = c;
    }

    public int getId() {
        return id;
    }
}

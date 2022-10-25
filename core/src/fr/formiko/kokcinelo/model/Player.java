package fr.formiko.kokcinelo.model;

public class Player {
    /** Unique id for every Player */
    private final int id;
    private static int idCpt = 0;
    private Creature playedCreature;
    private int score;

    public Player(Creature c) {
        id = idCpt++;
        playedCreature = c;
        score=0;
    }
    
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void addScore(int score){
        setScore(getScore()+score);
    }
    public Creature getPlayedCreature() {
        return playedCreature;
    }
    public void setPlayedCreature(Creature playedCreature) {
        this.playedCreature = playedCreature;
    }
    public int getId() {
        return id;
    }
}

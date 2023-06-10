package fr.formiko.kokcinelo.model;

/**
 * {@summary Player move a ladybug or an ant.}
 * Ant controling player figth ladybug controling player &#38; vis versa.
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class Player {
    /** Unique id for every Player */
    private final int id;
    private static int idCpt = 0;
    private Creature playedCreature;
    private int score;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Create a player with given creature.}
     * 
     * @param c player embodied creature
     */
    public Player(Creature c) {
        id = idCpt++;
        playedCreature = c;
        if (c instanceof Ant) {
            score = 100;
        } else {
            score = 0;
        }
    }

    // GET SET -------------------------------------------------------------------
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public void addScoreForLadybug(int score) {
        if (playedCreature instanceof Ant) {
            addScore(-score);
        } else {
            addScore(score);
        }
    }
    public void addScoreForAnt(int score) {
        if (!(playedCreature instanceof Ant)) {
            addScore(-score);
        } else {
            addScore(score);
        }
    }
    public void addScore(int score) { setScore(getScore() + score); }
    public Creature getPlayedCreature() { return playedCreature; }
    public void setPlayedCreature(Creature playedCreature) { this.playedCreature = playedCreature; }
    public int getId() { return id; }

    // FUNCTIONS -----------------------------------------------------------------
}

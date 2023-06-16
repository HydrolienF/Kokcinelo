package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.tools.Math;

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
        setPlayedCreature(c);
        if (c instanceof Ant || c instanceof Aphid) {
            score = 100;
            if (c instanceof Aphid) {
                c.setLastCollectedTime(0);
            }
        } else {
            score = 0;
        }
    }

    // GET SET -------------------------------------------------------------------
    public int getScore() { return score; }
    public void setScore(int score) { this.score = Math.between(0, Controller.getController().getGameState().getMaxScore(), score); }
    public void addScore(int score) { setScore(getScore() + score); }
    public Creature getPlayedCreature() { return playedCreature; }
    public void setPlayedCreature(Creature playedCreature) { this.playedCreature = playedCreature; }
    public int getId() { return id; }

    // FUNCTIONS -----------------------------------------------------------------
}

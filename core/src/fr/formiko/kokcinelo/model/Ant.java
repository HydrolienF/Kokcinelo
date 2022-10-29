package fr.formiko.kokcinelo.model;

/**
 * {@summary Ants are creatures that figth ladybugs to protect aphids.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class Ant extends Creature {
    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Create new Ants.}
     */
    public Ant() {
        // TODO to complete.
        super("ant");
        hitRadius = 50;
    }

    // GET SET -------------------------------------------------------------------
    @Override
    public float getMaxRotationPerSecond() { return 200f; }

    // FUNCTIONS -----------------------------------------------------------------
}

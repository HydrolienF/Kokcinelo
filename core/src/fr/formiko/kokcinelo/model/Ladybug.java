package fr.formiko.kokcinelo.model;

/**
 * {@summary Ladybugs are Creature that eat aphids.}
 * Usually ladybugs run away from ants.
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class Ladybug extends Creature {
    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor with default texture, high radius, low hit point &#38; high heal point.}
     */
    public Ladybug() {
        super("ladybug");
        visionRadius = 500;
        hitRadius = 100;
    }

    // GET SET -------------------------------------------------------------------
    @Override
    public int getGivenPoints() { return 10; }
    @Override
    public float getMaxRotationPerSecond() { return 140f; }
    // FUNCTIONS -----------------------------------------------------------------
}

package fr.formiko.kokcinelo.model;

import java.util.Set;

/**
 * {@summary Ladybugs are Creature that eat aphids.}
 * Usually ladybugs run away from ants.
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class Ladybug extends Creature {
    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor with default texture, high radius, low hit point &#38; high heal point.}
     */
    public Ladybug(String textureName) {
        super(textureName);
        visionRadius = 600;
        hearRadius = 1200;
        hitRadius = 100;
        movingSpeed = 5f;
        maxLifePoints = 100;
        lifePoints = maxLifePoints;
    }
    public Ladybug() { this("ladybug"); }

    // GET SET -------------------------------------------------------------------
    @Override
    public int getGivenPoints() { return 10; }
    @Override
    public float getMaxRotationPerSecond() { return 140f; }
    @Override
    public Set<Class<? extends Creature>> getCreaturesToHunt() { return Set.of(Aphid.class); }
    @Override
    public Set<Class<? extends Creature>> getCreaturesHuntedBy() { return Set.of(Ant.class); }
    // FUNCTIONS -----------------------------------------------------------------
}

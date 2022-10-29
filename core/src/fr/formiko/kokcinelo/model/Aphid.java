package fr.formiko.kokcinelo.model;

import java.util.Collection;

/**
 * {@summary Aphids are Creatures eated by ladybugs.}
 * Ant always try to protect them.
 * 
 * @author Hydrolien
 * @since 0.1
 * @version 0.1
 */
public class Aphid extends Creature {
    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main Constructor with all default params}:
     * aphid texture, low life points, no hit points, no shoot points, a small visionRadius &#38; a small hit radius.
     */
    public Aphid() {
        super("aphid");
        lifePoints = 1;
        hitPoints = 0;
        shootPoints = 0;
        visionRadius = 120;
        hitRadius = 20;
        movingSpeed = 1.5f;
    }

    // GET SET -------------------------------------------------------------------
    @Override
    public int getGivenPoints() { return 1; }
    @Override
    public float getMaxRotationPerSecond() { return 500f; }

    // FUNCTIONS -----------------------------------------------------------------
    public Ladybug closestLadybug(Collection<Ladybug> coll) {
        Ladybug closest = null;
        for (Ladybug ladybug : coll) {
            if (isInRadius(ladybug, ladybug.getHitRadius() + getVisionRadius())) {
                if (closest == null || distanceTo(ladybug) < distanceTo(closest)) {
                    closest = ladybug;
                }
            }
        }
        return closest;
    }
}

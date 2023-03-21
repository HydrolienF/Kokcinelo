package fr.formiko.kokcinelo.model;

import java.util.Set;
import com.badlogic.gdx.math.Vector2;

/**
 * {@summary Aphids are Creatures eated by ladybugs.}
 * Ant always try to protect them.
 * 
 * @author Hydrolien
 * @since 1.0
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
        maxLifePoints = 0;
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
    @Override
    public Set<Class<? extends Creature>> getCreaturesHuntedBy() { return Set.of(Ladybug.class); }

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Move aphids as AI.}
     * Aphids first run away from the closest ladybug they can see if they can see one.
     * Else they move slowly to a random direction &#38; some time change it.
     * If they hit a wall, they change there wanted rotation angle for the nexts turns.
     */
    public void moveAI(GameState gs) {
        Ladybug ladybug = (Ladybug) closestCreature(gs.getLadybugs());
        if (ladybug != null) {
            // Run away move
            runAwayFrom(new Vector2(ladybug.getCenterX(), ladybug.getCenterY()));
            moveFront();
        } else {
            // Normal move
            minorRandomRotation(0.02);
            moveFront(0.3f);
        }
        stayInMap(gs.getMapWidth(), gs.getMapHeight());
    }
}

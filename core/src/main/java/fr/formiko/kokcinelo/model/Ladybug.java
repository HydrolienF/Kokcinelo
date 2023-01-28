package fr.formiko.kokcinelo.model;

import com.badlogic.gdx.math.Vector2;

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
        visionRadius = 600;
        hitRadius = 100;
        movingSpeed = 5f;
    }

    // GET SET -------------------------------------------------------------------
    @Override
    public int getGivenPoints() { return 10; }
    @Override
    public float getMaxRotationPerSecond() { return 140f; }
    // FUNCTIONS -----------------------------------------------------------------
    public void moveAI(GameState gs) {
        Ant ant = (Ant) closestCreature(gs.getAnts());
        if (ant != null) {
            // Run away move
            runAwayFrom(new Vector2(ant.getCenterX(), ant.getCenterY()));
            moveFront();
        } else {
            Aphid aphid = (Aphid) closestCreature(gs.getAphids());
            if (aphid != null) {
                // Hunt move
                goTo(new Vector2(aphid.getCenterX(), aphid.getCenterY()));
                moveFront();
            } else {
                // Normal move
                minorRandomRotation(0.02);
                moveFront(0.8f);
            }
        }
        stayInMap(gs.getMapWidth(), gs.getMapHeight());
    }
}

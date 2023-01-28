package fr.formiko.kokcinelo.model;

import com.badlogic.gdx.math.Vector2;

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
        super("ant");
        visionRadius = 500;
        hitRadius = 50;
        movingSpeed = 3f;
    }

    // GET SET -------------------------------------------------------------------
    @Override
    public float getMaxRotationPerSecond() { return 200f; }

    // FUNCTIONS -----------------------------------------------------------------
    public void moveAI(GameState gs) {
        Ladybug ladybug = (Ladybug) closestCreature(gs.getLadybugs());
        if (ladybug != null) {
            // Hunt move
            goTo(new Vector2(ladybug.getCenterX(), ladybug.getCenterY()));
            moveFront();
        } else {
            // Normal move
            minorRandomRotation(0.02);
            moveFront(0.8f);
        }
        stayInMap(gs.getMapWidth(), gs.getMapHeight());
    }
}

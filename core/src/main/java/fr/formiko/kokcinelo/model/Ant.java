package fr.formiko.kokcinelo.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Slot;

/**
 * {@summary Ants are creatures that figth ladybugs to protect aphids.}
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class Ant extends Creature {
    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Create new Ant.}
     * 
     * @param thoraxColor color of the thorax.
     */
    public Ant(Color thoraxColor) {
        super("ant");
        visionRadius = 500;
        hearRadius = 1000;
        hitRadius = 50;
        movingSpeed = 4.5f;
        maxLifePoints = 0;
        hitFrequency = 1000;

        Slot colorSlot = getActor().getSkeleton().findSlot("thorax color");
        colorSlot.getColor().set(thoraxColor);
    }
    /***
     * {@summary Create new red Ant.}
     */
    public Ant() { this(new Color(0, 0, 0, 0)); }

    // GET SET -------------------------------------------------------------------
    @Override
    public float getMaxRotationPerSecond() { return 200f; }

    // FUNCTIONS -----------------------------------------------------------------
    public void moveAI(GameState gs) {
        Ladybug ladybug = (Ladybug) closestCreature(gs.getLadybugs());
        if (ladybug != null) {
            // Hunt move
            goTo(new Vector2(ladybug.getCenterX(), ladybug.getCenterY()));
            moveFront(0.6f);
        } else {
            // Normal move
            minorRandomRotation(0.02);
            moveFront(0.6f);
        }
        stayInMap(gs.getMapWidth(), gs.getMapHeight());
    }
}

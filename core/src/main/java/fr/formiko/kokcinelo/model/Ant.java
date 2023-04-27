package fr.formiko.kokcinelo.model;

import java.util.Set;
import com.badlogic.gdx.graphics.Color;
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

        if (getActor() != null && getActor().getSkeleton() != null) {
            Slot colorSlot = getActor().getSkeleton().findSlot("thorax color");
            colorSlot.getColor().set(thoraxColor);
        }
    }
    /***
     * {@summary Create new red Ant.}
     */
    public Ant() { this(new Color(0, 0, 0, 0)); }

    // GET SET -------------------------------------------------------------------
    @Override
    public float getMaxRotationPerSecond() { return 200f; }
    @Override
    public Set<Class<? extends Creature>> getCreaturesToHunt() { return Set.of(Ladybug.class); }

    // FUNCTIONS -----------------------------------------------------------------
}

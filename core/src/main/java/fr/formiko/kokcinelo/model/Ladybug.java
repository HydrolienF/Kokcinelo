package fr.formiko.kokcinelo.model;

import java.util.Collection;
import java.util.Set;
import com.badlogic.gdx.math.Vector2;

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
    public void moveAI(GameState gs) {
        // TODO use getCreaturesHuntedBy & a filter insted of getAnts.
        Collection<Creature> enemies = seeableCreatures(gs.getAnts());
        if (!enemies.isEmpty()) {
            // Run away move
            // enemies.add(); //TODO add walls if needed.
            runAwayFrom(enemies.stream().map(c -> new Vector2(c.getCenterX(), c.getCenterY())).toArray(Vector2[]::new));
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
                moveFront(0.6f);
            }
        }
        stayInMap(gs.getMapWidth(), gs.getMapHeight());
    }
}

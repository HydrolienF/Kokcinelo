package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.graphics.Color;

/**
 * {@summary Aphids are Creatures eated by ladybugs.}
 * Ant always try to protect them.
 * 
 * @author Hydrolien
 * @version 2.5
 * @since 0.1
 */
public class Aphid extends Creature {
    // private Color color;
    private static final Map<Class<? extends Aphid>, Color> typeColorMap = Map.of(Aphid.class, new Color(0.4f, 1f, 0f, 1f),
            SpeedAphid.class, Color.CYAN, HealthAphid.class, Color.RED, ScoreAphid.class, Color.GOLD, VisibilityAphid.class,
            new Color(0.05f, 0.25f, 0f, 1f));

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
        defaultMoveFrontSpeed = 0.3f;
        colorSkeleton();
        collectedFrequency = 20000;
        lastCollectedTime = System.currentTimeMillis() - (long) (Math.random() * collectedFrequency);
        setHoneydewVisibility(false);
    }

    // GET SET -------------------------------------------------------------------
    @Override
    public int getGivenPoints() { return 1; }
    @Override
    public float getMaxRotationPerSecond() { return 500f; }
    @Override
    public Set<Class<? extends Creature>> getCreaturesHuntedBy() { return Set.of(Ladybug.class); }
    @Override
    public float getAnimationSpeedMultiplier() { return 4f; } // Aphid are small so they need to move faster in animation for realisme.
    public Color getColor() { return typeColorMap.get(getClass()); }
    // public Color getColor() { return getColor(0f); } // it can be set with a small variation.
    // public Color getColor(float variation) {
    // if (color == null) {
    // color = new Color(Math.between(0f, 1f, typeColorMap.get(getClass()).r + MathUtils.random(-variation, variation)),
    // Math.between(0f, 1f, typeColorMap.get(getClass()).g + MathUtils.random(-variation, variation)),
    // Math.between(0f, 1f, typeColorMap.get(getClass()).b + MathUtils.random(-variation, variation)),
    // typeColorMap.get(getClass()).a);
    // }
    // return color;
    // }
    public boolean isHoneydewReady() { return canBeCollected() && Controller.getController().getLevel().isWidthHoneydew(); }
    @Override
    public Set<Class<? extends Creature>> getCreaturesFriendly() { return Set.of(Ant.class, Aphid.class); }
    @Override
    public Set<Class<? extends Creature>> getCreaturesFriendlyWithVisibility() { return Set.of(Ant.class, Aphid.class); }

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Color aphid skeleton with aphid color.}
     * It color each body part that contains "background" in its name.
     */
    public void colorSkeleton() {
        if (getActor() != null && getActor().getSkeleton() != null) {
            getActor().getSkeleton().getSlots().forEach(slot -> {
                if (slot.getAttachment() != null && slot.getAttachment().getName().contains("background")) {
                    slot.getColor().set(getColor());

                }
            });
        } else {
            App.log(3, "Aphid skeleton is null");
        }
    }

    /**
     * {@summary Give bonus to eater when aphid is eaten.}
     * 
     * @param eater The creature that eat the aphid.
     */
    public void bonusWhenEaten(Creature eater) {
        // Default bonus is nothing.
    }
    /**
     * {@summary Give bonus to collector when honeydew is collected.}
     * 
     * @param collector The creature that collect the honeydew.
     */
    protected void bonusWhenCollectHoneydew(Creature collector) {
        // Default bonus is nothing.
    }

    /**
     * Update honeydew visibility depending of last collected time.
     */
    public void updateHonewdewVisibility() { setHoneydewVisibility(isHoneydewReady()); }

    /**
     * {@summary Set honeydew visibility.}
     * It set the scale of honeydew bone to 0 or 1.
     * 
     * @param visible True if honeydew is visible, false otherwise.
     */
    public void setHoneydewVisibility(boolean visible) {
        if (getActor() != null && getActor().getSkeleton() != null) {
            if (visible) {
                getActor().getSkeleton().findBone("honeydew").setScale(1, 1);
            } else {
                getActor().getSkeleton().findBone("honeydew").setScale(0, 0);
            }
        }
    }

    /**
     * {@summary Collect honeydew of this.}
     * It set lastCollectedTime to current time and give bonus to collector.
     */
    public void collectHoneydew(Creature collector) {
        bonusWhenCollectHoneydew(collector);
        lastCollectedTime = System.currentTimeMillis();
    }
}

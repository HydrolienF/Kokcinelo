package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.tools.Math;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * {@summary Aphids are Creatures eated by ladybugs.}
 * Ant always try to protect them.
 * 
 * @author Hydrolien
 * @since 1.0
 * @version 0.1
 */
public class Aphid extends Creature {
    private Type type;
    private Color color;
    private enum Type {
        NORMAL, SPEED, HEALTH, SCORE, VISIBILITY
    }
    private static Map<Type, Color> typeColorMap = Map.of(Type.NORMAL, new Color(0.4f, 1f, 0f, 1f), Type.SPEED, Color.NAVY, Type.HEALTH,
            Color.RED, Type.SCORE, Color.GOLD, Type.VISIBILITY, Color.CYAN);

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
        type = Type.NORMAL;
        // type = Type.values()[MathUtils.random(0, Type.values().length - 1)]; // random color.
        colorSkeleton();
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
    public Color getColor() { return getColor(0f); } // it can be set with a small variation.
    public Color getColor(float variation) {
        if (color == null) {
            color = new Color(Math.between(0f, 1f, typeColorMap.get(type).r + MathUtils.random(-variation, variation)),
                    Math.between(0f, 1f, typeColorMap.get(type).g + MathUtils.random(-variation, variation)),
                    Math.between(0f, 1f, typeColorMap.get(type).b + MathUtils.random(-variation, variation)), typeColorMap.get(type).a);
        }
        return color;
    }

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
}

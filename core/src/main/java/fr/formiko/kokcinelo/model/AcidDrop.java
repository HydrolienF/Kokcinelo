package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.tools.KScreen;

/**
 * {@summary AcidDrop are creatures that can only move, hit then die.}
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class AcidDrop extends Creature {
    private float distanceBeforeHit;

    /**
     * {@summary Create a new acid drop.}
     * 
     * @param centerX           center x position
     * @param centerY           center y position
     * @param rotation          rotation
     * @param distanceBeforeHit distance before hit ground or creature
     * @param hitPoints         damage done when hiting
     */
    public AcidDrop(float centerX, float centerY, float rotation, float distanceBeforeHit, float hitPoints) {
        super("acidDrop");
        setRotation(rotation);
        setCenterX(centerX);
        setCenterY(centerY);
        this.distanceBeforeHit = distanceBeforeHit;
        movingSpeed = 13f;
        getActor().setZoom(0.05f);
        this.hitPoints = hitPoints;
        hitRadius = 10;
    }

    public float getDistanceBeforeHit() { return distanceBeforeHit; }

    /**
     * {@summary Move the acid drop &#38; decrease distance before hit ground or creature.}
     */
    @Override
    public int moveAI(GameState gs) {
        moveFront();
        distanceBeforeHit -= getMovingSpeed() * KScreen.getFPSRacio();
        return 0;
    }

}

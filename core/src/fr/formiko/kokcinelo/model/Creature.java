package fr.formiko.kokcinelo.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * {@summary Abstact class that represent Creature on the map.}
 * Creature can move, fly, hit, eat, died.
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public abstract class Creature extends MapItem {
    protected boolean fliing;
    protected float lifePoints;
    protected float hitPoints;
    protected float shootPoints;
    protected float visionRadius;
    protected int color;
    protected float movingSpeed;
    protected float wantedRotation;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     * 
     * @param textureName name of the texture to use from assets/images/
     */
    public Creature(String textureName) {
        super(textureName);
        wantedRotation = -1000f;
    }

    // GET SET -------------------------------------------------------------------
    public int getGivenPoints() { return 0; }
    public float getVisionRadius() { return visionRadius; }
    public float getMaxRotationPerSecond() { return 90f; }
    public float getMovingSpeed() { return movingSpeed; }
    public float getWantedRotation() { return wantedRotation; }
    public void setWantedRotation(float wantedRotation) { this.wantedRotation = wantedRotation; }

    // FUNCTIONS -----------------------------------------------------------------
    public boolean see(MapItem mi) { return isInRadius(mi, visionRadius); }
    /**
     * {@summary Move in the facing direction at speed percentOfSpeed.}
     * If a wantedRotation have been set, go to it.
     * 
     * @param percentOfSpeed percent of max speed.
     */
    public void moveFront(float percentOfSpeed) {
        if (getWantedRotation() != -1000f && (getWantedRotation() + 360) % 360 != (getActor().getRotation() + 360) % 360) {
            // if (this instanceof Aphid) {
            // System.out.println("rotate from " + (getActor().getRotation() + 360) % 360 + " to " + (getWantedRotation() + 360) % 360);
            // }
            rotateAStep();
        }
        getActor().moveFront(getMovingSpeed() * percentOfSpeed);
    }
    /***
     * {@summary Move in the facing direction at max speed.}
     */
    public void moveFront() { moveFront(1f); }
    /**
     * {@summary Rotate as mutch as possible between 2 frames.}
     */
    private void rotateAStep() {
        float previousRotation = getActor().getRotation() % 360;
        if (wantedRotation > 180) {
            wantedRotation -= 360;
        }
        // float rotationToDo = (wantedRotation - previousRotation) % 360;
        // if (this instanceof Aphid) {
        // System.out.println("rotation to do " + rotationToDo);
        // }
        float allowedRotation = Math.min(getMaxRotationPerSecond() * Gdx.graphics.getDeltaTime(), Math.abs(wantedRotation));
        if (wantedRotation > 0) {
            allowedRotation *= -1;
        }
        if (allowedRotation == wantedRotation) {
            wantedRotation = -1000f;
        }
        getActor().setRotation(previousRotation + allowedRotation);
    }
    /**
     * {@summary Set wanted rotation to go to v.}
     * 
     * @param v      contains coordinate of Point to got to
     * @param degdif Degre difference to go to
     */
    public void goTo(Vector2 v, float degdif) {
        // Update wantedRotation
        Vector2 v2 = new Vector2(v.x - getActor().getCenterX(), v.y - getActor().getCenterY());
        float previousRotation = getActor().getRotation() % 360;
        float newRotation = v2.angleDeg() - 90;
        float wantedRotation = (previousRotation - newRotation + 360 + degdif) % 360;
        setWantedRotation(wantedRotation);
    }
    /***
     * {@summary Set wanted rotation to go to v.}
     * 
     * @param v contains coordinate of Point to got to
     */
    public void goTo(Vector2 v) { goTo(v, 0f); }
    /***
     * {@summary Set wanted rotation to run away from v.}
     * To run away from we calculate angle to go to, then add 180 degre to go to the oposite direction.
     * 
     * @param v contains coordinate of Point to run away from
     */
    public void runAwayFrom(Vector2 v) { goTo(v, 180f); }
}

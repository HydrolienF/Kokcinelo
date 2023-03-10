package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.Controller;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * {@summary Abstact class that represent Creature on the map.}
 * Creature can move, fly, hit, eat, died.
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public abstract class Creature extends MapItem {
    protected boolean fliing;
    protected float maxLifePoints;
    protected float hitPoints;
    protected float shootPoints;
    protected float visionRadius;
    protected float hearRadius;
    protected int color;
    protected float movingSpeed;
    protected float wantedRotation;
    protected long lastHitTime;
    protected long lastShootTime;
    protected int hitFrequency;
    protected int shootFrequency;
    protected int shootRadius;

    protected float lifePoints;
    protected float currentSpeed;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     * 
     * @param textureName name of the texture to use from assets/images/
     */
    public Creature(String textureName) {
        super(textureName);
        wantedRotation = 0f;
    }

    // GET SET -------------------------------------------------------------------
    public int getGivenPoints() { return 0; }
    public float getVisionRadius() { return visionRadius; }
    public void setVisionRadius(float visionRadius) { this.visionRadius = visionRadius; }
    public float getHearRadius() { return hearRadius; }
    public void setHearRadius(float hearRadius) { this.hearRadius = hearRadius; }
    public float getMaxRotationPerSecond() { return 90f; }
    public float getMovingSpeed() { return movingSpeed; }
    public void setMovingSpeed(float movingSpeed) { this.movingSpeed = movingSpeed; }
    public float getCurrentSpeed() { return currentSpeed; }
    public void setCurrentSpeed(float currentSpeed) { this.currentSpeed = currentSpeed; }
    public float getWantedRotation() { return wantedRotation; }
    public void setWantedRotation(float wantedRotation) { this.wantedRotation = wantedRotation; }
    public float getLifePoints() { return lifePoints; }
    public void setLifePoints(float lifePoints) { this.lifePoints = lifePoints; }
    public float getMaxLifePoints() { return maxLifePoints; }
    public void setMaxLifePoints(float maxLifePoints) { this.maxLifePoints = maxLifePoints; }
    public float getHitPoints() { return hitPoints; }
    public void setHitPoints(float hitPoints) { this.hitPoints = hitPoints; }
    public float getShootPoints() { return shootPoints; }
    public void setShootPoints(float shootPoints) { this.shootPoints = shootPoints; }
    public int getShootRadius() { return shootRadius; }
    public void setShootRadius(int shootRadius) { this.shootRadius = shootRadius; }
    public Set<Class<? extends Creature>> getCreaturesToHunt() { return Set.of(); }
    public Set<Class<? extends Creature>> getCreaturesHuntedBy() { return Set.of(); }

    // FUNCTIONS -----------------------------------------------------------------
    public String toString() {
        return this.getClass().getSimpleName() + " : " + super.toString() + "\n" + "lifePoints : " + lifePoints + "\n" + "maxLifePoints : "
                + maxLifePoints + "\n" + "hitPoints : " + hitPoints + "\n" + "shootPoints : " + shootPoints + "\n" + "visionRadius : "
                + visionRadius + "\n" + "color : " + color + "\n" + "movingSpeed : " + movingSpeed + "\n" + "wantedRotation : "
                + wantedRotation + "\n" + "lastHitTime : " + lastHitTime + "\n" + "lastShootTime : " + lastShootTime + "\n"
                + "hitFrequency : " + hitFrequency + "\n" + "shootFrequency : " + shootFrequency + "\n";
    }
    public boolean see(MapItem mi) { return isInRadius(mi, visionRadius); }
    /**
     * {@summary Boost the creature.}
     */
    public void boost() {
        setHitPoints(getHitPoints() * 2);
        setShootPoints(getShootPoints() * 2);
        hitFrequency /= 3;
        movingSpeed *= 1.3f;
    }
    /**
     * {@summary Move in the facing direction at speed percentOfSpeed.}
     * If a wantedRotation have been set, go for it.
     * 
     * @param percentOfSpeed percent of max speed.
     */
    public void moveFront(float percentOfSpeed) {
        rotateAStep();
        currentSpeed = getMovingSpeed() * percentOfSpeed;
        getActor().moveFront(currentSpeed);
    }
    /***
     * {@summary Move in the facing direction at max speed.}
     */
    public void moveFront() { moveFront(1f); }
    /**
     * {@summary Rotate as mutch as possible between 2 frames.}
     * It don't rotate if no wanted rotation have been set.
     */
    private void rotateAStep() {
        if (wantedRotation == 0f) {
            return;
        }
        float previousRotation = getRotation() % 360;
        wantedRotation = (wantedRotation + 360) % 360;
        if (wantedRotation > 180) {
            wantedRotation -= 360;
        }
        float allowedRotation = Math.min(getMaxRotationPerSecond() * Gdx.graphics.getDeltaTime(), Math.abs(wantedRotation));
        if (wantedRotation > 0) {
            allowedRotation *= -1;
        }
        wantedRotation += allowedRotation;
        setRotation(previousRotation + allowedRotation);
    }
    /**
     * {@summary Set wanted rotation to go to v.}
     * 
     * @param v      contains coordinate of Point to got to
     * @param degdif Degre difference to go to
     */
    public void goTo(Vector2 v, float degdif) {
        // Update wantedRotation
        Vector2 v2 = new Vector2(v.x - getCenterX(), v.y - getCenterY());
        float previousRotation = getRotation() % 360;
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
     * @param vectorList contains coordinate of Points to run away from
     */
    public void runAwayFrom(Vector2... vectorList) {
        if (vectorList.length == 0) {
            return;
        } else if (vectorList.length == 1) {
            goTo(vectorList[0], 180f);
        } else if (vectorList.length > 2) {
            float x = 0;
            float y = 0;
            int cpt = 0;
            for (Vector2 v2 : vectorList) {
                x += v2.x;
                y += v2.y;
                cpt++;
            }
            runAwayFrom(new Vector2(x / cpt, y / cpt));
        }
    }

    // TODO #140 a way to fix it is to be able to run away from multiple enemis

    /**
     * {@summary Return the closest Creature from the collection.}
     * 
     * @param coll Collection to iterate over
     * @return the closest Creature from the collection
     */
    public Creature closestCreature(Collection<? extends Creature> coll) {
        Creature closest = null;
        for (Creature c : coll) {
            if (isInRadius(c, c.getHitRadius() + getVisionRadius())) {
                if (closest == null || distanceTo(c) < distanceTo(closest)) {
                    closest = c;
                }
            }
        }
        return closest;
    }
    public Collection<Creature> seeableCreatures(Collection<? extends Creature> coll) {
        Set<Creature> set = new HashSet<Creature>();
        for (Creature c : coll) {
            if (isInRadius(c, c.getHitRadius() + getVisionRadius())) {
                set.add(c);
            }
        }
        return set;
    }
    /**
     * {@summary move as AI.}
     * 
     * @param gs GameState to use
     */
    public abstract void moveAI(GameState gs);

    /**
     * {@summary Check if the Creature is in the map &#38; move inside if needed.}
     * It also change wantedRotation if a wall have been hit.
     * 
     * @param mapWidth  width of the map
     * @param mapHeigth height of the map
     */
    public void stayInMap(float mapWidth, float mapHeigth) {
        // if have been move to avoid wall
        if (moveIn(mapWidth, mapHeigth)) {
            if (getWantedRotation() == 0f) { // if have not already choose a new angle to get out.
                setWantedRotation((160f + (float) (Math.random() * 40)) % 360f);
            }
        }
    }
    /**
     * {@summary Sometime, rotate a bit, randomly.}
     * 
     * @param frequency double in [0,1]. Next to 0 it hapend only fiew time. Next to 1 almost all time.
     */
    public void minorRandomRotation(double frequency) {
        double r = Math.random() / (Gdx.graphics.getDeltaTime() * 100);
        if (r < frequency) { // randomize rotation
            setWantedRotation((float) (Math.random() * 40) - 20f);
        }
    }
    /**
     * @return true if this can hit other creatures
     */
    public boolean canHit() {
        if (hitPoints > 0 && (System.currentTimeMillis() - lastHitTime) > hitFrequency) {
            return true;
        }
        return false;
    }
    /**
     * {@summary Hit a Creature.}
     * 
     * @param c creature to hit
     */
    public void hit(Creature c) {
        c.setLifePoints(c.getLifePoints() - getHitPoints());
        lastHitTime = System.currentTimeMillis();
        // App.log(1,this + " hit " + c + " with " + getHitPoints() + " hit points. Creature still have " + c.getLifePoints() + " life
        // points.");
        getActor().animate("hit", 5);
        if (c.getLifePoints() <= 0) {
            c.die();
        }
    }
    /**
     * {@summary Die, remove from controller & play diing animation.}
     */
    public void die() {
        getActor().animate("die", 6);
        Controller.getController().addToRemove(this);
    }
    /**
     * @return true if this can shoot other creatures
     */
    public boolean canShoot() {
        if (shootPoints > 0 && (System.currentTimeMillis() - lastShootTime) > shootFrequency) {
            return true;
        }
        return false;
    }
    /**
     * {@summary Shoot a Creature.}
     */
    public void shoot() {
        lastShootTime = System.currentTimeMillis();
        // App.log(1, this + " shoot " + c);
    }
    /** Return true if is an AI. */
    public boolean isAI() { return !equals(Controller.getController().getPlayerCreature()); }
}

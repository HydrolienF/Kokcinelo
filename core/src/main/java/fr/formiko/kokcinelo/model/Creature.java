package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.tools.KScreen;
import fr.formiko.kokcinelo.tools.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;

/**
 * {@summary Abstact class that represent Creature on the map.}
 * Creature can move, fly, hit, eat, died.
 * 
 * @author Hydrolien
 * @version 2.5
 * @since 0.1
 */
public abstract class Creature extends MapItem {
    protected boolean fliing;
    protected float maxLifePoints;
    protected float hitPoints;
    protected float shootPoints;
    protected float visionRadius;
    protected float hearRadius;
    protected float movingSpeed;
    /** Wanted rotation relative to current one. */
    protected float wantedRotation;
    protected long lastHitTime;
    protected long lastShootTime;
    protected long lastCollectedTime;
    protected long lastRunTime;
    protected int hitFrequency;
    protected int shootFrequency;
    protected int shootRadius;
    protected int collectedFrequency;
    protected int runFrequency;

    protected float lifePoints;
    protected float currentSpeed;
    protected float defaultMoveFrontSpeed;
    protected boolean canFly;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     * 
     * @param textureName name of the texture to use from assets/images/
     */
    protected Creature(String textureName) {
        super(textureName);
        wantedRotation = 0f;
        defaultMoveFrontSpeed = 0.6f;
        collectedFrequency = -1;
    }

    // GET SET -------------------------------------------------------------------
    public int getGivenPoints() { return 0; }
    public float getVisionRadius() { return visionRadius; }
    public void setVisionRadius(float visionRadius) { this.visionRadius = visionRadius; }
    public float getHearRadius() { return hearRadius; }
    public void setHearRadius(float hearRadius) { this.hearRadius = hearRadius; }
    public float getMaxRotationPerSecond() { return 90f; }
    public float getMovingSpeed() { return movingSpeed; }
    // public float getMovingSpeedWithFPS() { return movingSpeed * KScreen.getFPSRacio(); }
    public void setMovingSpeed(float movingSpeed) { this.movingSpeed = movingSpeed; }
    public float getCurrentSpeed() { return currentSpeed; }
    public void setCurrentSpeed(float currentSpeed) { this.currentSpeed = currentSpeed; }
    public float getDefaultMoveFrontSpeed() { return defaultMoveFrontSpeed; }
    public void setDefaultMoveFrontSpeed(float defaultMoveFrontSpeed) { this.defaultMoveFrontSpeed = defaultMoveFrontSpeed; }
    public float getWantedRotation() { return wantedRotation; }
    public void setWantedRotation(float wantedRotation) { this.wantedRotation = wantedRotation; }
    public float getLifePoints() { return lifePoints; }
    /** Setter with max value. */
    public void setLifePoints(float lifePoints) {
        this.lifePoints = lifePoints;
        if (lifePoints > getMaxLifePoints()) {
            this.lifePoints = getMaxLifePoints();
        }
    }
    public float getMaxLifePoints() { return maxLifePoints; }
    public void setMaxLifePoints(float maxLifePoints) { this.maxLifePoints = maxLifePoints; }
    public float getHitPoints() { return hitPoints; }
    public void setHitPoints(float hitPoints) { this.hitPoints = hitPoints; }
    public float getShootPoints() { return shootPoints; }
    public void setShootPoints(float shootPoints) { this.shootPoints = shootPoints; }
    public int getShootRadius() { return shootRadius; }
    public void setShootRadius(int shootRadius) { this.shootRadius = shootRadius; }
    public float getAnimationSpeedMultiplier() { return 1f; }
    public int getCollectedFrequency() { return collectedFrequency; }
    public void setCollectedFrequency(int collectedFrequency) { this.collectedFrequency = collectedFrequency; }
    public long getLastCollectedTime() { return lastCollectedTime; }
    public void setLastCollectedTime(long lastCollectedTime) { this.lastCollectedTime = lastCollectedTime; }
    public Set<Class<? extends Creature>> getCreaturesToHunt() { return Set.of(); }
    public Set<Class<? extends Creature>> getCreaturesHuntedBy() { return Set.of(); }
    public Set<Class<? extends Creature>> getCreaturesFriendly() { return Set.of(getClass()); }
    public Set<Class<? extends Creature>> getCreaturesFriendlyWithVisibility() { return Set.of(getClass()); }
    public @Null String getSpaceActionName() { return null; }
    /**
     * @return progress of the creature action that can be done with space key.
     */
    public float getSpaceActionProgress() {
        float actionProgress = 0f;
        if (shootPoints > 0) {
            actionProgress = (System.currentTimeMillis() - lastShootTime) / (float) shootFrequency;
        }
        if (runFrequency > 0) {
            actionProgress = (System.currentTimeMillis() - lastRunTime) / (float) runFrequency;
        }
        // TODO add ability to fly for level 5
        return Math.between(0f, 1f, actionProgress);
    }
    // public static float getZoomMin() { return 1f; }
    // public static float getZoomMax() { return 1f; }
    /**
     * Creature witch hunt this &#38; this can see it.
     */
    public Set<Creature> getVisibleCreatureHuntedBy() {
        if (getCreaturesHuntedBy().isEmpty()) {
            return Set.of();
        }
        // @formatter:off
        return Controller.getController().allCreatures().stream()
                .filter(c -> c.isInstanceOf(getCreaturesHuntedBy()))
                .filter(this::see)
                .collect(HashSet::new, Set::add, Set::addAll);
        // @formatter:on
    }
    /**
     * Creature witch are hunted by this &#38; this can see it.
     */
    public Creature getClosestVisibleCreatureToHunt() {
        if (getCreaturesToHunt().isEmpty()) {
            return null;
        }
        // @formatter:off
        return Controller.getController().allCreatures().stream()
                .filter(c -> c.isInstanceOf(getCreaturesToHunt()))
                .filter(this::see)
                // Compare distance to this.
                .min((c1, c2) -> Float.compare(distanceTo(c1), distanceTo(c2)))
                .orElse(null);
        // @formatter:on
    }
    /**
     * @return All friendly creatures of this.
     */
    public Set<Creature> getAllFriendlyCreature() {
        return Controller.getController().allCreatures().stream().filter(this::isFriendly).collect(HashSet::new, Set::add, Set::addAll);
    }
    /**
     * @return All friendly creatures of this witch this can see.
     */
    public Set<Creature> getAllFriendlyWithVisibilityCreature() {
        return Controller.getController().allCreatures().stream().filter(this::isFriendlyWithVisibility).collect(HashSet::new, Set::add,
                Set::addAll);
    }
    public boolean isFriendly(Creature c) { return c.isInstanceOf(getCreaturesFriendly()); }
    public boolean isFriendlyWithVisibility(Creature c) { return c.isInstanceOf(getCreaturesFriendlyWithVisibility()); }

    // FUNCTIONS -----------------------------------------------------------------
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " : " + super.toString() + "\n" + "lifePoints : " + lifePoints + "\n" + "maxLifePoints : "
                + maxLifePoints + "\n" + "hitPoints : " + hitPoints + "\n" + "shootPoints : " + shootPoints + "\n" + "visionRadius : "
                + visionRadius + "\n" + "movingSpeed : " + movingSpeed + "\n" + "wantedRotation : " + wantedRotation + "\n"
                + "lastHitTime : " + lastHitTime + "\n" + "lastShootTime : " + lastShootTime + "\n" + "hitFrequency : " + hitFrequency
                + "\n" + "shootFrequency : " + shootFrequency + "\n";
    }
    /** Do this see mi */
    public boolean see(MapItem mi) { return isInRadius(mi, visionRadius + mi.getHitRadius()); }
    public boolean isTooCloseForStart(MapItem mi) { return isInRadius(mi, visionRadius * 2f + mi.getHitRadius()); }
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
        // If last run time is less than 1s ago, multiply speed by 3.
        float runMultiplier = lastRunTime + 1000 > System.currentTimeMillis() ? 3f : 1f;
        currentSpeed = getMovingSpeed() * percentOfSpeed * runMultiplier;
        getActor().moveFront(currentSpeed * KScreen.getFPSRacio());
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
        float allowedRotation = java.lang.Math.min(getMaxRotationPerSecond() * Gdx.graphics.getDeltaTime(),
                java.lang.Math.abs(wantedRotation));
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
        Vector2 v2 = new Vector2(v.x - getCenterX(), v.y - getCenterY());
        float newRotation = v2.angleDeg() - 90;
        goTo(newRotation - degdif);
    }
    /**
     * {@summary Set wanted rotation to reach newRotation.}
     * 
     * @param newRotation rotation to reach
     */
    public void goTo(float newRotation) { setWantedRotation((getRotation() - newRotation + 360) % 360); }
    /***
     * {@summary Set wanted rotation to go to v.}
     * 
     * @param v contains coordinate of Point to got to
     */
    public void goTo(Vector2 v) { goTo(v, 0f); }
    public void goTo(MapItem mi) { goTo(mi.getCenter()); }
    /**
     * {@summary Set wanted rotation to run away from v.}
     * To run away from we calculate angle to each enemy &#38; wall.
     * Then we run away from the biggest angle.
     * 
     * @param vectorList      contains coordinate of Points to run away from
     * @param forbiddenAngles contains angle to avoid as wall direction
     */
    public void runAwayFrom(List<Float> forbiddenAngles, Vector2... vectorList) {
        // No enemy -> do nothing.
        // 1 enemy & no wall -> easy to run away.
        if (vectorList.length == 1 && forbiddenAngles.isEmpty()) {
            goTo(vectorList[0], 180f);
            // More than 1 enemy or wall -> find a good way to run away.
        } else if (vectorList.length > 1 || !forbiddenAngles.isEmpty()) {
            // Run by the biggest angle between 2 enemies.
            List<Float> angles = new ArrayList<>();
            for (Vector2 v : vectorList) {
                Vector2 vAngle = new Vector2(v.x - getCenterX(), v.y - getCenterY());
                angles.add(vAngle.angleDeg());
            }
            // Also avoid wall by conciderning walls as enemies.
            addForbiddenAngleAsEnemisAngles(forbiddenAngles, angles);

            angles.sort(Float::compare);

            float maxAngleDif = 0;
            float direction = 0;
            List<Float> anglesDif = new ArrayList<>();
            float lastAngle = angles.get(angles.size() - 1);
            for (float angle : angles) {
                float angleDif = (angle - lastAngle + 360) % 360;
                if (angleDif > maxAngleDif) {
                    maxAngleDif = angleDif;
                    direction = angle - angleDif / 2;
                }
                anglesDif.add(angleDif);
                lastAngle = angle;
            }
            App.log(0, "forbiddenAngles : " + forbiddenAngles);
            App.log(0, "angles : " + angles);
            App.log(0, "anglesDif : " + anglesDif);
            App.log(0, "maxanglesDif : " + maxAngleDif);
            App.log(0, "direction:" + direction);
            goTo(direction - 90);
        }
    }

    /**
     * {@summary Add forbidden angles as enemies angles.}
     * 
     * @param forbiddenAngles angles to avoid
     * @param angles          enemies angles where to add new enemis angles
     */
    private void addForbiddenAngleAsEnemisAngles(List<Float> forbiddenAngles, List<Float> angles) {
        if (!forbiddenAngles.isEmpty()) {
            angles.add(forbiddenAngles.get(0));
            if (forbiddenAngles.size() > 1) {
                float a0 = forbiddenAngles.get(0);
                float a1 = forbiddenAngles.get(1);
                if (forbiddenAngles.get(0) == 0 && forbiddenAngles.get(1) == 270) { // patch for the angle 270 to 0.
                    a0 = 270;
                    a1 = 360;
                }
                // Add more enemies angle between the 2 forbidden angles to avoid to go into the corner.
                // (Add 7, one for each 10° should be enoth.)
                for (float i = a0 + 10; i < a1; i += 10) {
                    angles.add(i);
                }
            }
        }
    }

    /**
     * {@summary Return the closest Creature from the collection.}
     * 
     * @param coll Collection to iterate over
     * @return the closest Creature from the collection
     */
    public Creature closestCreature(Collection<? extends Creature> coll) {
        Creature closest = null;
        for (Creature c : coll) {
            if ((isInRadius(c, c.getHitRadius() + getVisionRadius())) && (closest == null || distanceTo(c) < distanceTo(closest))) {
                closest = c;
            }
        }
        return closest;
    }
    /**
     * {@summary Return all the seeable Creature from the collection.}
     * 
     * @param coll Collection to iterate over
     * @return the seeable creatures
     */
    public Collection<Creature> seeableCreatures(Collection<? extends Creature> coll) {
        Set<Creature> set = new HashSet<>();
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
    public int moveAI(GameState gs) {
        int moveStatus;
        Collection<Creature> enemies = getVisibleCreatureHuntedBy();
        if (!enemies.isEmpty()) {
            // Run away move
            Vector2[] vectors = enemies.stream().map(MapItem::getCenter).toArray(Vector2[]::new);
            runAwayFrom(getWallsAngles(), vectors);
            moveFront();
            moveStatus = 2;
        } else {
            Creature target = getClosestVisibleCreatureToHunt();
            if (target != null) {
                // Hunt move
                goTo(target);
                moveFront();
                moveStatus = 1;
            } else {
                // Normal move
                minorRandomRotation(0.02);
                moveFront(defaultMoveFrontSpeed);
                moveStatus = 0;
            }
        }
        stayInMap(gs.getMapWidth(), gs.getMapHeight());
        return moveStatus;
    }
    /**
     * {@summary Return the angle of wall.}
     * It is used to avoid wall when running away by adding them as enemies.
     * 
     * @return the angle of wall (sorted)
     */
    public List<Float> getWallsAngles() {
        List<Float> wallList = new ArrayList<>();
        float distanceToWall = getVisionRadius() / 2;
        // @formatter:off
        // If there is a really close wall, seach for a second one a bit further.
        if(getCenterX() + distanceToWall > Controller.getController().getGameState().getMapWidth()
                || getCenterY() + distanceToWall > Controller.getController().getGameState().getMapHeight()
                || getCenterX() - distanceToWall < 0
                || getCenterY() - distanceToWall < 0){
            distanceToWall = getVisionRadius();
        }
        // @formatter:on

        if (getCenterX() + distanceToWall > Controller.getController().getGameState().getMapWidth()) {
            wallList.add(0f);
        }
        if (getCenterY() + distanceToWall > Controller.getController().getGameState().getMapHeight()) {
            wallList.add(90f);
        }
        if (getCenterX() - distanceToWall < 0) {
            wallList.add(180f);
        }
        if (getCenterY() - distanceToWall < 0) {
            wallList.add(270f);
        }

        return wallList;
    }

    /**
     * {@summary Check if the Creature is in the map &#38; move inside if needed.}
     * It also change wantedRotation if a wall have been hit.
     * 
     * @param mapWidth  width of the map
     * @param mapHeight height of the map
     */
    public void stayInMap(float mapWidth, float mapHeight) {
        // if have been move to avoid wall & if have not already choose a new angle to get out.
        if (moveIn(mapWidth, mapHeight) && getWantedRotation() == 0f) {
            setWantedRotation((160f + (float) (java.lang.Math.random() * 40)) % 360f);
        }
    }
    /**
     * {@summary Sometime, rotate a bit, randomly.}
     * It will move by max 20° from the current rotation.
     * 
     * @param frequency double in [0,1]. Next to 0 it hapend only fiew time. Next to 1 almost all time.
     */
    public void minorRandomRotation(double frequency) {
        double r = java.lang.Math.random() / (Gdx.graphics.getDeltaTime() * 100);
        if (r < frequency) { // randomize rotation
            setWantedRotation((float) (java.lang.Math.random() * 40) - 20f);
        }
    }
    /**
     * @return true if this can hit other creatures
     */
    public boolean canHit() { return (hitPoints > 0 && (System.currentTimeMillis() - lastHitTime) > hitFrequency); }
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
     * {@summary Die, remove from controller &#38; play diing animation.}
     */
    public void die() {
        getActor().animate("die", 6);
        Controller.getController().addToRemove(this);
        if (!isAI()) { // TODO find another way to do this in multiplayer
            Controller.getController().gameOver();
        }
    }
    /**
     * @return true if this can shoot other creatures
     */
    public boolean canShoot() { return (shootPoints > 0 && (System.currentTimeMillis() - lastShootTime) > shootFrequency); }

    public boolean canRun() { return (runFrequency > 0 && (System.currentTimeMillis() - lastRunTime) > runFrequency); }

    public boolean canBeCollected() {
        return collectedFrequency >= 0 && (System.currentTimeMillis() - lastCollectedTime) > collectedFrequency;
    }
    /**
     * {@summary Shoot a Creature.}
     */
    public void shoot() {
        lastShootTime = System.currentTimeMillis();
        getActor().animate("shoot", 6);
        // App.log(1, this + " shoot " + c);
    }

    public void run() { lastRunTime = System.currentTimeMillis(); }
    /** Return true if is an AI. */
    public boolean isAI() { return !equals(Controller.getController().getPlayerCreature()); }

    /**
     * {@summary Add time to values that need it.}
     * It is used when the game is resume to avoid that creature can hit &#38; shoot again even if game time have'nt run.
     * 
     * @param timePaused time that have run bewteen pause &#38; resume
     */
    public void addTime(long timePaused) {
        super.addTime(timePaused);
        lastHitTime += timePaused;
        lastShootTime += timePaused;
        lastCollectedTime += timePaused;
        lastRunTime = +timePaused;
    }

    /**
     * Add score to this creature team.
     */
    public void addScore(int score) {
        // If player see this creature as a friend, add score to player
        if (Controller.getController().getPlayerCreature().isFriendly(this)) {
            Controller.getController().getLocalPlayer().addScore(score);
        } else { // else remove score to player
            Controller.getController().getLocalPlayer().addScore(-score);
        }
    }

    /**
     * {@summary Return true if is instance of one of the class.}
     * 
     * @param coll Collection to iterate over
     * @return true if is instance of one of the class
     */
    private boolean isInstanceOf(Collection<Class<? extends Creature>> coll) {
        for (Class<? extends Creature> clss : coll) {
            if (clss.isInstance(this)) {
                return true;
            }
        }
        return false;
    }
}

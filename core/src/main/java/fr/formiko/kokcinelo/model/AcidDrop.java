package fr.formiko.kokcinelo.model;

public class AcidDrop extends Creature {
    private float distanceBeforeHit;

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
        distanceBeforeHit -= getMovingSpeed();
        return 0;
    }

}

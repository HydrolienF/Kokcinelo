package fr.formiko.kokcinelo.model;

/**
 * {@summary Abstact class that represent Creature on the map.}
 * Creature can move, fly, hit, eat, died.
 */
public abstract class Creature extends MapItem {
    protected boolean fliing;
    protected float lifePoints;
    protected float hitPoints;
    protected float shootPoints;
    protected float visionRadius;
    protected int color;

    public Creature(String textureName) { super(textureName); }

    public int getGivenPoints() { return 0; }
    public float getVisionRadius() { return visionRadius; }
    public float getMaxRotationPerSecond() { return 90f; }

    public boolean see(MapItem mi) { return isInRadius(mi, visionRadius); }
}

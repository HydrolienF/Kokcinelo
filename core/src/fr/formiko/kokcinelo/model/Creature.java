package fr.formiko.kokcinelo.model;

public abstract class Creature extends MapItem {
    protected boolean fliing;
    protected float lifePoints;
    protected float hitPoints;
    protected float shootPoints;
    protected float visionRadius;
    protected int color;
    // protected int scorePoints;

    public float getVisionRadius() { return visionRadius; }

    public Creature(String textureName) { super(textureName); }

    public int getGivenPoints() { return 0; }

    // public void addScorePoints(int score) {
    // scorePoints += score;
    // }

    public boolean see(MapItem mi) { return isInRadius(mi, visionRadius); }
}

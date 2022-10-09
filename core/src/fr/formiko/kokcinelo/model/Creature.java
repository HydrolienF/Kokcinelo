package fr.formiko.kokcinelo.model;

public abstract class Creature extends MapItem {
    protected boolean fliing;
    protected double lifePoints;
    protected double hitPoints;
    protected double shootPoints;
    protected double visionRadius;
    protected int color;
    protected int scorePoints;

    public Creature() {
        super();
    }

    public int getGivenPoints() {
        return 0;
    }

    public void addScorePoints(int score) {
        scorePoints += score;
    }

    public boolean see(MapItem mi) {
        return isInRadius(mi, visionRadius);
    }
}

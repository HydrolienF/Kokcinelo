package fr.formiko.kokcinelo.model;

public class Aphid extends Creature {
    public Aphid() {
        super("aphid");
        lifePoints = 1;
        hitPoints = 0;
        shootPoints = 0;
        visionRadius = 120;
        hitRadius = 20;
    }

    @Override
    public int getGivenPoints() { return 1; }
    @Override
    public float getMaxRotationPerSecond() { return 500f; }

}

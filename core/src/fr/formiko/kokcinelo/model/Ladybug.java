package fr.formiko.kokcinelo.model;

public class Ladybug extends Creature {
    public Ladybug() {
        super("ladybug");
        visionRadius = 500;
        hitRadius = 100;
    }

    @Override
    public int getGivenPoints() {
        return 10;
    }
}

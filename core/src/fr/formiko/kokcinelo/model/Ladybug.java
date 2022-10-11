package fr.formiko.kokcinelo.model;

public class Ladybug extends Creature {
    public Ladybug() {
        super("ladybug");
        visionRadius = 500;
    }

    @Override
    public int getGivenPoints() {
        return 10;
    }
}

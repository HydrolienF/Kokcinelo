package fr.formiko.kokcinelo.model;

public class Ladybug extends Creature {
    public Ladybug() {
        super("ladybug");
    }

    @Override
    public int getGivenPoints() {
        return 10;
    }
}

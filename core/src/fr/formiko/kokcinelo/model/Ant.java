package fr.formiko.kokcinelo.model;

public class Ant extends Creature {
    public Ant() {
        super("ant");
        hitRadius = 50;
    }

    @Override
    public float getMaxRotationPerSecond() { return 200f; }

}

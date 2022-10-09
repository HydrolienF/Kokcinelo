package fr.formiko.kokcinelo.model;

public class Aphid extends Creature {
    public Aphid() {
        super("aphid");
        lifePoints = 1;
        hitPoints = 0;
        shootPoints = 0;
    }

    @Override
    public int getGivenPoints() {
        return 1;
    }

}

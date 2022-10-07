package fr.formiko.kokcinelo.gamedata;

public class Aphid extends Creature {
    public Aphid() {
        lifePoints = 1;
        hitPoints = 0;
        shootPoints = 0;
    }

    @Override
    public int getGivenPoints() {
        return 1;
    }

}

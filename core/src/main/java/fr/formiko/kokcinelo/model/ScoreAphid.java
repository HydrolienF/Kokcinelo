package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.Controller;

public class ScoreAphid extends Aphid {
    public ScoreAphid() { super(); }
    @Override
    public int getGivenPoints() { return 10; }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) { Controller.getController().addScoreForLadybug(-2); }
}
package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.Controller;

public class BigScoreAphid extends ScoreAphid {
    public BigScoreAphid() { super(); }
    @Override
    public int getGivenPoints() { return 50; }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) { Controller.getController().addScoreForLadybug(-10); }
}
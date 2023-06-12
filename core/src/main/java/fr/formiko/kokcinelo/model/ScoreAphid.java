package fr.formiko.kokcinelo.model;

public class ScoreAphid extends Aphid {
    @Override
    public int getGivenPoints() { return 10; }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) { collector.addScore(2); }
}

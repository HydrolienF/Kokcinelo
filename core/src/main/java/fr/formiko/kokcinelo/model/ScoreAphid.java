package fr.formiko.kokcinelo.model;

public class ScoreAphid extends Aphid {
    public ScoreAphid() { super(); }
    @Override
    public int getGivenPoints() { return 10; }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) { collector.addScore(2); }
}

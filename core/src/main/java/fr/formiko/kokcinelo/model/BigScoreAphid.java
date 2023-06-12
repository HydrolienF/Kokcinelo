package fr.formiko.kokcinelo.model;

public class BigScoreAphid extends ScoreAphid {
    public BigScoreAphid() {
        super();
        visionRadius *= 3f;
    }
    @Override
    public int getGivenPoints() { return 50; }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) { collector.addScore(10); }
}

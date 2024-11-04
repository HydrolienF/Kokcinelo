package fr.formiko.kokcinelo.model;

public class FlyingAphid extends Aphid {
    public FlyingAphid(){
        super();
        canFly = true;
    }
    @Override
    public int getGivenPoints() { return 10; }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) { collector.addScore(2); }
}

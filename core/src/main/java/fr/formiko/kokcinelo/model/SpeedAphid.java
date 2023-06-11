package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;

public class SpeedAphid extends Aphid {
    public SpeedAphid() { super(); }
    @Override
    public void bonusWhenEaten(Creature eater) {
        App.log(1, "BONUS", "Give eat bonus: " + getClass().getSimpleName() + " to " + eater.getId());
        eater.setMovingSpeed(eater.getMovingSpeed() * 1.03f);
    }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) {
        App.log(1, "BONUS", "Give collect honeydew bonus: " + getClass().getSimpleName() + " to " + collector.getId());
        collector.setMovingSpeed(collector.getMovingSpeed() * 1.03f);
    }
}

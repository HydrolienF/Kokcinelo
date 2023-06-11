package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;

public class HealthAphid extends Aphid {
    public HealthAphid() { super(); }
    @Override
    public void bonusWhenEaten(Creature eater) {
        App.log(1, "BONUS", "Give eat bonus: " + getClass().getSimpleName() + " to " + eater.getId());
        eater.setLifePoints(eater.getLifePoints() + eater.getMaxLifePoints() / 10f);
    }
    @Override
    public void bonusWhenCollectHoneydew(Creature collector) {
        App.log(1, "BONUS", "Give collect honeydew bonus: " + getClass().getSimpleName() + " to " + collector.getId());
        collector.setHitPoints(collector.getHitPoints() * 1.03f);
        collector.setShootPoints(collector.getShootPoints() * 1.03f);
    }
}

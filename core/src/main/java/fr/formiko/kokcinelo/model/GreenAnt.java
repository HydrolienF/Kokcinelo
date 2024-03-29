package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * {@summary Ants are creatures that figth ladybugs to protect aphids.}
 * This type of Ant use acid to kill ladybugs.
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class GreenAnt extends Ant {
    /**
     * {@summary Create new Ants.}
     */
    public GreenAnt() {
        super(new Color(MathUtils.random(0f, 0.2f), MathUtils.random(0.8f, 1), MathUtils.random(0f, 0.2f), 1));
        shootPoints = 25;
        shootFrequency = 2000;
        shootRadius = (int) visionRadius + 200;
        if (getActor() != null && getActor().getSkeleton() != null) {
            getActor().getSkeleton().setSkin("green");
        } else {
            App.log(3, "GreenAnt skeleton is null");
        }
    }

    @Override
    public String getSpaceActionName() { return "AcidShootAction"; }

    @Override
    public void shoot() {
        super.shoot();
        Controller.getController().playSound("shoot", this);
        // Create new acid drop
        AcidDrop ad = new AcidDrop(getCenterX(), getCenterY(), getRotation(), getShootRadius(), getShootPoints());
        App.log(0, "New acid drop with distance before hit: " + ad.getDistanceBeforeHit());
        Controller.getController().getGameState().getAcidDrops().add(ad);
        Controller.getController().getGameScreen().getStage().addActor(ad.getActor());
    }
}

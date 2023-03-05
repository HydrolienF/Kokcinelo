package fr.formiko.kokcinelo.model;

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
        getActor().getSkeleton().setSkin("green");
    }
}

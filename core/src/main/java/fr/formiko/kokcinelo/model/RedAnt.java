package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * {@summary Ants are creatures that figth ladybugs to protect aphids.}
 * This type of Ant use mandibles to kill ladybugs.
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 1.1
 */
public class RedAnt extends Ant {
    private static final float headSize = 1.4f;
    private static final float antennaSize = 1.1f / headSize; // /headSize to keep the same size.
    /**
     * {@summary Create a red ant by using rigth skeleton size &#38; color.}
     */
    public RedAnt() {
        super(new Color(MathUtils.random(0.8f, 1), MathUtils.random(0f, 0.2f), MathUtils.random(0f, 0.2f), 1));
        if (getActor() != null && getActor().getSkeleton() != null) {
            // add big mandibles.
            getActor().getSkeleton().findBone("head").setScale(headSize);
            getActor().getSkeleton().findBone("antenna r").setScale(antennaSize);
            getActor().getSkeleton().findBone("antenna l").setScale(antennaSize);
            getActor().getSkeleton().setSkin("red");
        } else {
            App.log(3, "RedAnt skeleton is null");
        }
        hitPoints = 20;
    }

}

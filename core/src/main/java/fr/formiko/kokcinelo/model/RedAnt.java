package fr.formiko.kokcinelo.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class RedAnt extends Ant {
    private static final float headSize = 1.4f;
    private static final float antennaSize = 1.1f / headSize; // /headSize to keep the same size.
    public RedAnt() {
        super(new Color(MathUtils.random(0.8f, 1), MathUtils.random(0f, 0.2f), MathUtils.random(0f, 0.2f), 1));
        getActor().getSkeleton().findBone("head").setScale(headSize);
        getActor().getSkeleton().findBone("antenna r").setScale(antennaSize);
        getActor().getSkeleton().findBone("antenna l").setScale(antennaSize);
        // TODO add big mandibles.
        getActor().getSkeleton().setSkin("red");
        hitPoints = 20;
    }

}

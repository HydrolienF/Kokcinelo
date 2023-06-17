package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import com.badlogic.gdx.graphics.Color;

/**
 * {@summary Blood spot are created when a creature die.}
 * 
 * @author Hydrolien
 * @version 2.5
 * @since 2.5
 */
public class BloodSpot extends MapItem {
    private Color color;
    /**
     * {@summary Create a new blood spot.}
     * Blood spot are created when a creature die.
     * 
     * @param centerX  center x position
     * @param centerY  center y position
     * @param rotation rotation
     * @param zoom     zoom
     * @param color    color of this actor
     */
    public BloodSpot(float centerX, float centerY, float rotation, float zoom, Color color) {
        super("bloodSpot");
        this.color = color;
        setRotation(rotation);
        setCenterX(centerX);
        setCenterY(centerY);
        getActor().setZoom(zoom);
        colorSkeleton();
    }
    public BloodSpot(Aphid aphid) { this(aphid.getCenterX(), aphid.getCenterY(), aphid.getRotation(), aphid.getZoom(), aphid.getColor()); }

    /**
     * {@summary Color blood spot.}
     */
    public void colorSkeleton() {
        if (getActor() != null && getActor().getSkeleton() != null) {
            getActor().getSkeleton().findSlot("bloodSpot").getColor().set(color);
        } else {
            App.log(3, "BloodSpot skeleton is null");
        }
    }
}

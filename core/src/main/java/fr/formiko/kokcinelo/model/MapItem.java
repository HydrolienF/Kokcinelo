package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.tools.Math;
import fr.formiko.kokcinelo.view.MapItemActor;

/**
 * {@summary Abstact class that represent any thing on the map.}
 * Moving map item are Creature.
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public abstract class MapItem {
    /** Unique id for every MapItem */
    private final int id;
    private static int idCpt = 0;
    private MapItemActor actor;
    /** hit radius is used as an hitBox for interaction */
    protected int hitRadius;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Create a new MapItem with an actor using texture.}
     * 
     * @param textureName name of the texture to use from assets/images/
     */
    public MapItem(String textureName) {
        id = idCpt++;
        actor = new MapItemActor(textureName, this);
    }
    /**
     * {@summary Only for test constructor.}
     * 
     * @param width  width of Map
     * @param height height of Map
     */
    protected MapItem(int width, int height) {
        id = idCpt++;
        actor = new MapItemActor(this, width, height);
    }

    // GET SET -------------------------------------------------------------------
    public int getId() { return id; }
    public int getHitRadius() { return hitRadius; }
    public MapItemActor getActor() { return actor; }
    // Actor allowed getter / setter.
    public float getCenterX() { return getActor().getCenterX(); }
    public float getCenterY() { return getActor().getCenterY(); }
    public boolean moveIn(float maxX, float maxY) { return getActor().moveIn(maxX, maxY); }
    public float getRotation() { return getActor().getRotation(); }
    public void setRotation(float degrees) { getActor().setRotation(degrees); }
    public void setRandomLoaction(float maxX, float maxY) { getActor().setRandomLoaction(maxX, maxY); }
    public void setZoom(float zoom) { getActor().setZoom(zoom); }
    public void removeActor() { getActor().remove(); }

    // FUNCTIONS -----------------------------------------------------------------
    @Override
    public String toString() { return "" + id; }
    @Override
    public int hashCode() { return id; }
    /**
     * {@summary Equals function that compare class then id.}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MapItem other = (MapItem) obj;
        if (id != other.id)
            return false;
        return true;
    }

    /***
     * Return true if hit box of the 2 MapItems are connected.
     */
    public boolean hitBoxConnected(MapItem it) { return isInRadius(it, hitRadius + it.hitRadius); }

    /***
     * Return true if other MapItem is in radius.
     */
    public boolean isInRadius(MapItem mi2, double radius) { return distanceTo(mi2) < radius; }

    /**
     * {@summary Return the distance between center point of this &#38; center point
     * of an other MapItem.}
     */
    public float distanceTo(MapItem mi2) {
        return (float) Math.getDistanceBetweenPoints(getCenterX(), getCenterY(), mi2.getCenterX(), mi2.getCenterY());
    }
}

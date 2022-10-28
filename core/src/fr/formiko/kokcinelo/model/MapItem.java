package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.view.MapItemActor;
import fr.formiko.tools.Math;

/**
 * {@summary Abstact class that represent any thing on the map.}
 * Moving map item are Creature.
 */
public abstract class MapItem {
    /** Unique id for every MapItem */
    private final int id;
    private static int idCpt = 0;
    private MapItemActor actor;
    /** hit radius is used */
    protected int hitRadius;

    /**
     * {@summary Create a new MapItem with an actor using texture.}
     * 
     * @param textureName name of the texture to use from assets/images/
     */
    public MapItem(String textureName) {
        id = idCpt++;
        actor = new MapItemActor(textureName, this);
    }

    public int getId() { return id; }
    public int getHitRadius() { return hitRadius; }
    public MapItemActor getActor() { return actor; }
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
        return (float) Math.getDistanceBetweenPoints(getActor().getCenterX(), getActor().getCenterY(), mi2.getActor().getCenterX(),
                mi2.getActor().getCenterY());
    }
}

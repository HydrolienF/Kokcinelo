package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.view.MapItemActor;
import fr.formiko.tools.Math;

public abstract class MapItem {
    /** Unique id for every MapItem */
    private final int id;
    private static int idCpt = 0;
    private MapItemActor actor;
    protected int hitRadius;

    public MapItemActor getActor() {
        return actor;
    }

    public MapItem(String textureName) {
        id = idCpt++;
        actor = new MapItemActor(textureName, this);
    }

    public String toString() {
        return "" + id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

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

    public boolean hitBoxConnected(MapItem it) {
        return isInRadius(it, hitRadius + it.hitRadius);
    }

    public boolean isInRadius(MapItem mi2, double radius) {
        float dist = distanceTo(mi2);
        return dist < radius;
    }

    public float distanceTo(MapItem mi2) {
        return (float) Math.getDistanceBetweenPoints(getActor().getCenterX(), getActor().getCenterY(),
                mi2.getActor().getCenterX(), mi2.getActor().getCenterY());
    }
}

package fr.formiko.kokcinelo.gamedata;

public abstract class MapItem {
    /** Unique id for every MapItem */
    private final int id;
    private static int idCpt = 0;
    // private Vector3 location;
    // size
    // direction

    public MapItem() {
        id = idCpt++;
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

    public boolean hitBixConnected(MapItem it) {
        // TODO
        return false;
    }
}

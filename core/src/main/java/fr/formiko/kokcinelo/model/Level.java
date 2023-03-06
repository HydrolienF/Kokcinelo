package fr.formiko.kokcinelo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@summary Level of the game.}
 * It contains a unique id &#38; fiew useful methods.
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
public class Level {
    private final String id;
    private static final Set<Level> levelList = Set.of(newLevel("1K"), newLevel("2K"), newLevel("3K"), newLevel("4K"), newLevel("5K"),
            newLevel("2F"), newLevel("3F"), newLevel("4F"), newLevel("5F"), newLevel("4A"), newLevel("5A"));
    private List<Level> nextLevels;

    private Level(String id) { this.id = id; }
    private static Level newLevel(String id) { return new Level(id); }

    public String getId() { return id; }
    public static Set<Level> getLevelList() { return levelList; }
    /**
     * {@summary Get a level from its id.}
     * 
     * @param id id of the level to get
     * @return the level
     */
    public static Level getLevel(String id) {
        for (Level l : levelList) {
            if (l.getId().equals(id)) {
                return l;
            }
        }
        return null;
    }
    public int getNumber() { return Integer.parseInt(getId().substring(0, 1)); }
    public String getLetter() { return getId().substring(1, 2); }
    /**
     * {@summary Return the next levels.}
     * If next level haven't been calculated, it calculate it.
     * 
     * @return a list of the next levels
     */
    public List<Level> getNextLevels() {
        if (nextLevels == null) {
            nextLevels = new ArrayList<Level>();
            Level next = getLevel((getNumber() + 1) + getLetter());
            if (next != null) {
                nextLevels.add(next);
            }
            if (getLetter().equals("K")) {
                for (String letter : List.of("F", "A")) {
                    next = getLevel(getNumber() + 1 + letter);
                    Level shouldNotExist = getLevel(getNumber() + letter);
                    if (next != null && shouldNotExist == null) {
                        nextLevels.add(next);
                    }
                }
            }
        }
        return nextLevels;
    }

    /**
     * {@summary Return a string representation of the level.}
     */
    @Override
    public String toString() {
        String r = getId();
        r += "(";
        for (Level level : getNextLevels()) {
            r += level.getId() + ", ";
        }
        r += ")";
        return r;
    }
}

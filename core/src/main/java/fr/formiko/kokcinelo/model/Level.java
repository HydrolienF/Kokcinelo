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
public final class Level {
    private final String id;
    private boolean unlocked;
    private static final Set<Level> levelList = Set.of(newLevel("1K"), newLevel("2K"), newLevel("3K"), newLevel("4K"), newLevel("5K"),
            newLevel("2F"), newLevel("3F"), newLevel("4F"), newLevel("5F"), newLevel("4A"), newLevel("5A"));
    private static final Set<String> levelLetters = Set.of("K", "F", "A");
    private List<Level> nextLevels;

    private Level(String id) { this.id = id; }
    private static Level newLevel(String id) { return new Level(id); }

    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    public String getId() { return id; }
    public static Set<Level> getLevelList() { return levelList; }
    public int getNumber() { return Integer.parseInt(getId().substring(0, 1)); }
    public String getLetter() { return getId().substring(1, 2); }
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

    /**
     * {@summary Return the next levels.}<br>
     * If next level haven't been calculated, it calculate it.<br>
     * Every level can be unlocked by completing the previous level.
     * Previous level of 3X is 2X if it exist, else 2K.
     * 
     * @return a list of the next levels
     */
    public List<Level> getNextLevels() {
        if (nextLevels == null) { // initialize by lazy
            nextLevels = new ArrayList<Level>();
            addNextLevelsFromLine();
            if (getLetter().equals("K")) { // K can unlock other letters levels
                addNextLevelsFromOtherLine();
            }
        }
        return nextLevels;
    }
    /** Add the next level on same line. */
    private void addNextLevelsFromLine() {
        Level next = getLevel((getNumber() + 1) + getLetter());
        if (next != null) {
            nextLevels.add(next);
        }
    }
    /** Add the next levels from the other lines. */
    private void addNextLevelsFromOtherLine() {
        for (String letter : levelLetters) {
            if (!letter.equals(getLetter())) { // not same letter
                Level next = getLevel(getNumber() + 1 + letter);
                if (next != null && getLevel(getNumber() + letter) == null) {
                    nextLevels.add(next);
                }
            }
        }
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

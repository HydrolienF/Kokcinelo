package fr.formiko.kokcinelo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.utils.Null;

/**
 * {@summary Level of the game.}
 * It contains a unique id &#38; fiew useful methods.
 * 
 * @author Hydrolien
 * @version 1.1
 * @since 0.2
 */
public final class Level {
    private final String id;
    private final Map<Class<? extends Creature>, Integer> creaturesToSpawn;
    private final List<Class<? extends Creature>> playerCreatureClasses;
    private int playerCreatureClassIndex;
    private boolean unlocked;
    // @formatter:off
    private static final Set<Level> levelList = Set.of(
            newLevel("1K", Map.of(Aphid.class, 100, Ladybug.class, 1)),
            newLevel("2K", Map.of(Aphid.class, 100, Ladybug.class, 1, RedAnt.class, 3)),
            newLevel("3K", Map.of(Aphid.class, 100, Ladybug.class, 1, GreenAnt.class, 3)),
            newLevel("4K", Map.of(Ladybug.class, 1, RedAnt.class, 2, GreenAnt.class, 2,
                    Aphid.class, 50, ScoreAphid.class, 3, SpeedAphid.class, 10, HealthAphid.class, 10)),
            newLevel("5K", List.of(FlyingLadybug.class), Map.of(FlyingLadybug.class, 1, RedAnt.class, 2, GreenAnt.class, 2,
                    Aphid.class, 50, FlyingAphid.class, 3, SpeedAphid.class, 10, HealthAphid.class, 10)),
            newLevel("2F", List.of(RedAnt.class), Map.of(Aphid.class, 100, Ladybug.class, 2, RedAnt.class, 1)),
            newLevel("3F", List.of(GreenAnt.class), Map.of(Aphid.class, 100, Ladybug.class, 2, GreenAnt.class, 1)),
            newLevel("4F", List.of(RedAnt.class, GreenAnt.class), Map.of(Ladybug.class, 3, RedAnt.class, 1, GreenAnt.class, 1,
                    Aphid.class, 50, ScoreAphid.class, 3, SpeedAphid.class, 10, HealthAphid.class, 10)),
            newLevel("5F", List.of(RedAnt.class, GreenAnt.class), Map.of(FlyingLadybug.class, 3, RedAnt.class, 1, GreenAnt.class, 1,
                    Aphid.class, 50, FlyingAphid.class, 3, SpeedAphid.class, 10, HealthAphid.class, 10)),
            newLevel("4A", List.of(BigScoreAphid.class), Map.of(Ladybug.class, 4, RedAnt.class, 2, Aphid.class, 50, BigScoreAphid.class, 1)),
            newLevel("5A", List.of(FlyingBigScoreAphid.class), Map.of(FlyingLadybug.class, 4, RedAnt.class, 2, Aphid.class, 50, FlyingBigScoreAphid.class, 1)));
    // @formatter:on
    private static final Set<String> levelLetters = Set.of("K", "F", "A");
    private @Null List<Level> nextLevels; // Use getter with lazy initialization.

    /**
     * {@summary Create a new level by creation reference to args object.}
     * 
     * @param id               Id of the level.
     * @param creaturesToSpawn Number of creatures to spawn by class.
     */
    private Level(String id, List<Class<? extends Creature>> playerCreatureClasses,
            Map<Class<? extends Creature>, Integer> creaturesToSpawn) {
        this.id = id;
        this.creaturesToSpawn = creaturesToSpawn;
        this.playerCreatureClasses = playerCreatureClasses;
        playerCreatureClassIndex = 0;
    }
    /**
     * {@summary Builder.}
     * If there is no aphids in Creature to swawn, it add the default number, 100 aphids.
     * 
     * @param id               Id of the level.
     * @param creaturesToSpawn Number of creatures to spawn by class.
     * @return a new level.
     */
    private static Level newLevel(String id, List<Class<? extends Creature>> playerCreatureClasses,
            Map<Class<? extends Creature>, Integer> creaturesToSpawn) {
        return new Level(id, playerCreatureClasses, creaturesToSpawn);
    }
    private static Level newLevel(String id, Map<Class<? extends Creature>, Integer> creaturesToSpawn) {
        return newLevel(id, null, creaturesToSpawn);
    }
    private static Level newLevel(String id) { return newLevel(id, null, Map.of()); }

    /** To use only for testing */
    public static Level newTestLevel(String id, List<Class<? extends Creature>> playerCreatureClasses,
            Map<Class<? extends Creature>, Integer> creaturesToSpawn) {
        return newLevel(id, playerCreatureClasses, creaturesToSpawn);
    }


    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    public String getId() { return id; }
    public static Set<Level> getLevelList() { return levelList; }
    public int getNumber() { return Integer.parseInt(getId().substring(0, 1)); }
    public String getLetter() { return getId().substring(1, 2); }
    public int getPlayerCreatureClassIndex() { return playerCreatureClassIndex; }
    public void setPlayerCreatureClassIndex(int playerCreatureClassIndex) { this.playerCreatureClassIndex = playerCreatureClassIndex; }
    /**
     * {@summary Return the creature class that player can play.}
     */
    public List<Class<? extends Creature>> getPlayerCreatureClasses() {
        if (playerCreatureClasses == null) {
            switch (getLetter()) {
                case "K":
                    return List.of(Ladybug.class);
                case "F":
                    return List.of(RedAnt.class);
                case "A":
                    return List.of(Aphid.class);
                default:
                    return List.of(Ladybug.class);
            }
        } else {
            return playerCreatureClasses;
        }
    }
    public Class<? extends Creature> getPlayerCreatureClass() { return getPlayerCreatureClasses().get(playerCreatureClassIndex); }
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
     * {@summary Get the draw priority of the level.}
     */
    public int getDrawPriority() {
        switch (getLetter()) {
            case "K":
                return 1;
            case "F":
                return 2;
            case "A":
                return 0;
            default:
                return -1;
        }
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
            nextLevels = new ArrayList<>();
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

    public Map<Class<? extends Creature>, Integer> getCreaturesToSpawn() { return creaturesToSpawn; }

    /**
     * {@summary Return a string representation of the level.}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("(");
        boolean first = true;
        for (Level level : getNextLevels()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(level.getId());
        }
        sb.append(")");
        return sb.toString();
    }
}

package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.view.MapActor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Null;

/**
 * {@summary Class that containts all data about current game.}
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 0.1
 */
public class GameState {
    private final Map<Class<? extends MapItem>, List<? extends MapItem>> mapItems;
    private final List<Aphid> aphids;
    private final List<Ant> ants;
    private final List<Ladybug> ladybugs;
    private final List<Player> players;
    private final List<AcidDrop> acidDrops;
    private MapActor mapActorBg;
    private MapActor mapActorFg;
    private int maxScore;
    private int localPlayerId;
    private Level level;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Private main constructor.}
     * Use GameState.builder() to create a new GameState.
     */
    private GameState() {
        aphids = new ArrayList<>();
        ants = new ArrayList<>();
        ladybugs = new ArrayList<>();
        players = new ArrayList<>();
        acidDrops = new LinkedList<>();
        mapItems = new HashMap<>(10);
        mapItems.put(BloodSpot.class, new ArrayList<BloodSpot>());
    }

    // GET SET -------------------------------------------------------------------
    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
    public int getScore() { return getPlayer(getLocalPlayerId()).getScore(); }
    public void setScore(int score) { getPlayer(getLocalPlayerId()).setScore(score); }
    public int getPercentScore() { return (getScore() * 100) / getMaxScore(); }
    public MapActor getMapActorFg() { return mapActorFg; }
    public MapActor getMapActor() { return mapActorBg; }
    public List<Aphid> getAphids() { return aphids; }
    public List<Ant> getAnts() { return ants; }
    public List<Ladybug> getLadybugs() { return ladybugs; }
    public List<AcidDrop> getAcidDrops() { return acidDrops; }
    @SuppressWarnings("unchecked")
    public List<BloodSpot> getBloodSpots() { return (List<BloodSpot>) mapItems.get(BloodSpot.class); }
    public int getLocalPlayerId() { return localPlayerId; }
    public void setLocalPlayerId(int localPlayerId) { this.localPlayerId = localPlayerId; }
    public Level getLevel() { return level; }
    public String getLevelId() { return getLevel().getId(); }
    private void setLevel(Level level) { this.level = level; }
    /**
     * {@summary Return player from the list of player or null if not found.}
     * 
     * @param playerId id of the player
     * @return player that match playerId
     */
    public Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }
    /**
     * {@summary Return player creature or null if not found.}
     * 
     * @param playerId id of the player
     * @return Creature that player embody.
     */
    public @Null Creature getPlayerCreature(int playerId) {
        Player p = getPlayer(playerId);
        if (p != null) {
            return p.getPlayedCreature();
        }
        return null;
    }
    public float getMapWidth() { return getMapActor().getWidth(); }
    public float getMapHeight() { return getMapActor().getHeight(); }

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Add a Creature to a list of Creature where class match.}
     * 
     * @param c Creature to add
     */
    public void addCreature(Creature c) {
        if (c instanceof Aphid aphid) {
            aphids.add(aphid);
        } else if (c instanceof Ant ant) {
            ants.add(ant);
        } else if (c instanceof Ladybug ladybug) {
            ladybugs.add(ladybug);
        }
    }

    /**
     * Return all Creatures.
     * Removing item from this Iterable will have no impact on Collection where Creature are stored.
     * Use controller.toRemove.add(c) if you need to remove a creature
     * 
     * @return All creatures: aphids + ants + ladybugs + acid drop
     */
    public Collection<Creature> allCreatures() {
        List<Creature> l = new LinkedList<>();
        l.addAll(aphids);
        l.addAll(ladybugs);
        l.addAll(ants);
        l.addAll(acidDrops);
        return l;
    }
    /**
     * Return all MapItems.
     */
    public Collection<MapItem> allMapItems() {
        List<MapItem> l = new LinkedList<>();
        l.addAll(getBloodSpots());
        l.addAll(allCreatures());
        return l;
    }
    /**
     * Return ants &#38; ladybugs
     * Removing item from this Iterable will have no impact on Collection where Creature are stored.
     * Use controller.toRemove.add(c) if you need to remove a creature
     * 
     * @return ants &#38; ladybugs
     */
    public Collection<Creature> getAntsAndLadybugs() {
        List<Creature> l = new LinkedList<>();
        l.addAll(ants);
        l.addAll(ladybugs);
        return l;
    }
    /**
     * {@summary Return all the actor that GameState have.}
     * It is used to add them to stage.
     * 
     * @return all actors to draw
     */
    public Collection<Actor> allActors() {
        List<Actor> l = new LinkedList<>();
        if (mapActorBg != null) {
            mapActorBg.setName("background");
            l.add(mapActorBg);
        }
        for (MapItem mapItem : allMapItems()) {
            if (mapItem.getActor() != null) {
                l.add(mapItem.getActor());
            }
        }
        if (mapActorFg != null) {
            mapActorFg.setName("foreground");
            l.add(mapActorFg);
        }
        return l;
    }
    /**
     * {@summary Update actor visibility so that the only visible actor are the one close enoth to the player.}
     * 
     * @param playerId current player id
     */
    public void updateActorVisibility(int playerId, boolean spectatorMode) {
        Creature playedCreature = getPlayerCreature(playerId);
        if (playedCreature != null) {
            for (MapItem mapItem : allMapItems()) {
                mapItem.getActor().setVisible(playedCreature.see(mapItem) || spectatorMode);
            }
        }
    }
    /**
     * {@summary Return most importants variables.}
     * 
     * @return Most importants variables as String
     */
    @Override
    public String toString() {
        return "GameState [mapCoordinate=" + mapActorBg + ", aphids=" + aphids + ", ants=" + ants + ", ladybugs=" + ladybugs + ", players="
                + players + "]" + " " + mapActorBg + " " + mapActorFg;
    }

    public boolean isAllAphidGone() { return aphids.isEmpty(); }
    public boolean isAllLadybugGone() { return ladybugs.isEmpty(); }

    /**
     * {@summary Remove a Creature from the list of Creature where class match &#38; from the Stage.}
     */
    public void remove(Creature c) {
        if (c instanceof Aphid) {
            aphids.remove(c);
        } else if (c instanceof Ant) {
            ants.remove(c);
        } else if (c instanceof Ladybug) {
            ladybugs.remove(c);
        } else if (c instanceof AcidDrop) {
            acidDrops.remove(c);
        }
        c.removeActor();
    }
    /**
     * {@summary Remove a collection of Creature from the list of Creature where class match &#38; from the Stage.}
     */
    public void remove(Collection<Creature> creatures) {
        for (Creature c : creatures) {
            remove(c);
        }
    }

    /**
     * {@summary Try to move some AI Creature (ladybugs &#38; Ants) away from player.}
     * It is used to give a more fair start.
     */
    public void moveAIAwayFromPlayers() {
        for (Player player : players) {
            Creature pc = player.getPlayedCreature();
            for (Creature c : getAntsAndLadybugs()) {
                if (c.isAI()) {
                    int k = 0;
                    while (k < 50 && pc.isTooCloseForStart(c)) {
                        App.log(1, "Move out " + c.getId() + " to avoid player to see it");
                        c.setRandomLoaction(getMapWidth(), getMapHeight());
                        k++;
                    }
                    if (pc.isTooCloseForStart(c)) {
                        App.log(2, "Can't find a location for c in " + k + " tries");
                    }
                }
            }
        }
    }

    // static
    public static GameStateBuilder builder() { return new GameStateBuilder(); }

    // Builder ------------------------------------------------------------------------------------------------------------
    /**
     * {@summary Builder to use to get a GameState.}
     * 
     * @author Hydrolien
     * @version 0.1
     * @since 0.1
     */
    public static class GameStateBuilder {
        private int mapWidth;
        private int mapHeight;
        private GameState gs;
        private Level level;

        private GameStateBuilder() {}

        /**
         * {@summary Build a new GameState.}
         * It have some check &#38; fix to avoid futur error, as map coordinate that
         * can't be less than 1.
         * 
         * @return a new GameState
         */
        public GameState build() {
            gs = new GameState();

            if (level == null) {
                level = Level.getLevel("1K");
            }
            gs.setLevel(level);

            // initialize default game
            addMapBackground();
            addCreatures();
            Player p = new Player(
                    gs.allCreatures().stream().filter(c -> c.getClass().equals(level.getPlayerCreatureClass())).findFirst().orElseThrow());
            if (Controller.isDebug()) {
                p.getPlayedCreature().setVisionRadius(mapWidth);
            }
            if (level.getNumber() > 1) {
                // boost player so that it can manage several enemies.
                p.getPlayedCreature().boost();
            }

            gs.players.add(p);
            gs.setLocalPlayerId(p.getId());
            addMapForeground();

            gs.setMaxScore(gs.getAphids().stream().mapToInt(a -> a.getGivenPoints()).sum());

            return gs;
        }
        /**
         * {@summary Setter that return same GameStateBuilder to alow chained setter.}
         * 
         * @return current GameStateBuilder
         */
        public GameStateBuilder setMapWidth(int mapWidth) {
            this.mapWidth = mapWidth;
            return this;
        }
        /**
         * {@summary Setter that return same GameStateBuilder to alow chained setter.}
         * 
         * @return current GameStateBuilder
         */
        public GameStateBuilder setMapHeight(int mapHeight) {
            this.mapHeight = mapHeight;
            return this;
        }
        /**
         * {@summary Setter that return same GameStateBuilder to alow chained setter.}
         * 
         * @return current GameStateBuilder
         */
        public GameStateBuilder setLevel(Level level) {
            this.level = level;
            return this;
        }


        // private ------------------------------------------------------------------
        /**
         * {@summary Add all Creatures}
         * 
         * @param aphidsNumber   number of aphids to play with
         * @param ladybugsNumber number of ladybugs to play with
         * @param antsNumber     number of ants to play with
         */
        private void addCreatures(int aphidsNumber, int ladybugsNumber, int redAntNumber, int greenAntNumber) {
            addC(aphidsNumber, 0.02f, 0.04f, true, true, Aphid.class);
            addC(ladybugsNumber, 0.4f, 0.4f, true, true, Ladybug.class);
            float antSize = 0.05f;
            addC(redAntNumber, antSize, antSize, true, true, RedAnt.class);
            addC(greenAntNumber, antSize, antSize, true, true, GreenAnt.class);
        }
        /**
         * {@summary Add all Creatures from level data.}
         */
        private void addCreatures() {
            final Map<Class<? extends Creature>, Vector2> zoom = Map.of(Aphid.class, new Vector2(0.02f, 0.04f), Ladybug.class,
                    new Vector2(0.4f, 0.4f), Ant.class, new Vector2(0.05f, 0.05f));
            for (Entry<Class<? extends Creature>, Integer> entry : level.getCreaturesToSpawn().entrySet()) {
                float min = 1f;
                float max = 1f;
                for (Entry<Class<? extends Creature>, Vector2> c : zoom.entrySet()) {
                    if (c.getKey().isAssignableFrom(entry.getKey())) {
                        min = c.getValue().x;
                        max = c.getValue().y;
                        break;
                    }
                }
                addC(entry.getValue(), min, max, true, true, entry.getKey());
            }
        }

        /**
         * {@summary Add a class of Creature.}
         * 
         * @param numberToAdd     number of creatures to play with
         * @param zoomMin         mininum zoom on added creatures
         * @param zoomMax         maximum zoom on added creatures
         * @param randomLocaction if true, place creatures in random location on map
         * @param randomRotation  if true creatures have random roation
         * @param creatureClass   Class of the creatures to add
         */
        private void addC(final int numberToAdd, final float zoomMin, final float zoomMax, final boolean randomLocaction,
                final boolean randomRotation, final Class<? extends Creature> creatureClass) {
            for (int i = 0; i < numberToAdd; i++) {
                try {
                    Creature c = creatureClass.getDeclaredConstructor().newInstance();

                    if (randomLocaction) {
                        c.setRandomLoaction(gs.getMapWidth(), gs.getMapHeight());
                    }
                    if (randomRotation) {
                        c.setRotation((float) Math.random() * 360f);
                    }
                    if (zoomMin > 0) {
                        if (zoomMax > zoomMin) {
                            c.setZoom(zoomMin + (float) Math.random() * (zoomMax - zoomMin));
                        } else {
                            c.setZoom(zoomMin);
                        }
                    }
                    gs.addCreature(c);
                    // HTML INCOMPATIBLE
                    // } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    // | NoSuchMethodException | SecurityException e) {
                } catch (Exception e) {
                    // } catch (IllegalArgumentException | SecurityException | NullPointerException e) {
                    StringWriter out = new StringWriter();
                    PrintWriter pw = new PrintWriter(out);
                    e.printStackTrace(pw);
                    App.log(3, "Fail to add a new Creature " + e + "\n" + out.toString());
                    pw.close();
                }
            }
        }

        /**
         * {@summary Add the map background.}
         */
        private void addMapBackground() {
            gs.mapActorBg = new MapActor(Math.max(1, mapWidth), Math.max(1, mapHeight), App.GREEN, true);
            // gs.mapActorBg = new MapActor(Math.max(1, mapWidth), Math.max(1, mapHeight),
            // Color.OLIVE);
        }
        /**
         * {@summary Add the map foreground as a black mask.}
         */
        private void addMapForeground() {
            gs.mapActorFg = new MapActor(Math.max(1, mapWidth * 2), Math.max(1, mapHeight * 2), Color.BLACK, false);
            gs.mapActorFg.addToExclude(0f, 0f, gs.getPlayerCreature(gs.getLocalPlayerId()).getVisionRadius());
        }
    }
}

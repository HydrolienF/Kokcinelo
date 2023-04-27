package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.view.MapActor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * {@summary Class that containts all data about current game.}
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class GameState {
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
        aphids = new ArrayList<Aphid>();
        ants = new ArrayList<Ant>();
        ladybugs = new ArrayList<Ladybug>();
        players = new ArrayList<Player>();
        acidDrops = new LinkedList<AcidDrop>();
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
    public Creature getPlayerCreature(int playerId) {
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
        if (c instanceof Aphid) {
            aphids.add((Aphid) c);
        } else if (c instanceof Ant) {
            ants.add((Ant) c);
        } else if (c instanceof Ladybug) {
            ladybugs.add((Ladybug) c);
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
        List<Creature> l = new LinkedList<Creature>();
        l.addAll(aphids);
        l.addAll(ladybugs);
        l.addAll(ants);
        l.addAll(acidDrops);
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
        List<Creature> l = new LinkedList<Creature>();
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
        List<Actor> l = new LinkedList<Actor>();
        if (mapActorBg != null) {
            mapActorBg.setName("background");
            l.add(mapActorBg);
        }
        for (Creature creature : allCreatures()) {
            if (creature.getActor() != null) {
                l.add(creature.getActor());
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
            for (Creature creature : allCreatures()) {
                creature.getActor().setVisible(playedCreature.see(creature) || spectatorMode);
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
     * @summary Remove a Creature from the list of Creature where class match & from the Stage.
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
     * @summary Remove a collection of Creature from the list of Creature where class match & from the Stage.
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
                    while (k < 20 && (pc.see(c) || c.see(pc))) {
                        App.log(1, "Move out " + c.getId() + " to avoid player to see it");
                        c.setRandomLoaction(getMapWidth(), getMapHeight());
                        k++;
                    }
                    if (pc.see(c) || c.see(pc)) {
                        App.log(2, "Can't find a location for c in " + k + " try");
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
        private int greenAntNumber;
        private int redAntNumber;
        private int aphidNumber;
        private int ladybugNumber;
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

            if (aphidNumber < 1) {
                aphidNumber = 1;
            }
            gs.setMaxScore(aphidNumber);

            if (level == null) {
                level = Level.getLevel("1K");
            }
            gs.setLevel(level);

            // initialize default game
            addMapBackground();
            addCreatures(aphidNumber, ladybugNumber, redAntNumber, greenAntNumber);
            Player p;
            switch (level.getLetter()) {
            case "K":
                p = new Player(gs.ladybugs.get(0));
                break;
            case "F":
                p = new Player(gs.ants.get(0));
                break;
            case "A":
                p = new Player(gs.aphids.get(0));
                break;
            default:
                App.log(3, "incorect level id, switch to default Creature : ladybug (K)");
                p = new Player(gs.ladybugs.get(0));
                break;
            }
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

            // System.out.println(gs);
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
        public GameStateBuilder setRedAntNumber(int antNumber) {
            this.redAntNumber = antNumber;
            return this;
        }
        /**
         * {@summary Setter that return same GameStateBuilder to alow chained setter.}
         * 
         * @return current GameStateBuilder
         */
        public GameStateBuilder setGreenAntNumber(int antNumber) {
            this.greenAntNumber = antNumber;
            return this;
        }
        /**
         * {@summary Setter that return same GameStateBuilder to alow chained setter.}
         * 
         * @return current GameStateBuilder
         */
        public GameStateBuilder setAphidNumber(int aphidNumber) {
            this.aphidNumber = aphidNumber;
            return this;
        }
        /**
         * {@summary Setter that return same GameStateBuilder to alow chained setter.}
         * 
         * @return current GameStateBuilder
         */
        public GameStateBuilder setLadybugNumber(int ladybugNumber) {
            this.ladybugNumber = ladybugNumber;
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
            addC(aphidsNumber, 0.1f, 0.2f, true, true, Aphid.class);
            addC(ladybugsNumber, 0.4f, 0.4f, true, true, Ladybug.class);
            float antSize = 0.05f;
            addC(redAntNumber, antSize, antSize, true, true, RedAnt.class);
            addC(greenAntNumber, antSize, antSize, true, true, GreenAnt.class);
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
                    Creature c = creatureClass.getDeclaredConstructor().newInstance(); // HTML INCOMPATIBLE
                    // version to replace last line that work in html
                    // Creature c = null;
                    // if (creatureClass.toString().endsWith("Aphid")) {
                    // c = new Aphid();
                    // } else if (creatureClass.toString().endsWith("Ant")) {
                    // c = new Ant();
                    // } else if (creatureClass.toString().endsWith("Ladybug")) {
                    // c = new Ladybug();
                    // }
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
                    PrintWriter pw = new PrintWriter(new StringWriter());
                    e.printStackTrace(pw);
                    App.log(3, "Fail to add a new Creature " + e + "\n" + pw.toString());
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

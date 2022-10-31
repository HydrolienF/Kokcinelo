package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.view.MapActor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * {@summary Class that containts all data about current game.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class GameState {
    private List<Aphid> aphids;
    private List<Ant> ants;
    private List<Ladybug> ladybugs;
    private List<Player> players;
    private MapActor mapActorBg;
    private MapActor mapActorFg;
    private int maxScore;

    // CONSTRUCTORS --------------------------------------------------------------
    /***
     * {@summary Private main constructor.}
     * Use GameState.builder() to create a new GameState.
     */
    private GameState() {
        aphids = new ArrayList<Aphid>();
        ants = new ArrayList<Ant>();
        ladybugs = new ArrayList<Ladybug>();
        players = new ArrayList<Player>();
    }

    // GET SET -------------------------------------------------------------------
    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
    public MapActor getMapActorFg() { return mapActorFg; }
    public MapActor getMapActor() { return mapActorBg; }
    public List<Aphid> getAphids() { return aphids; }
    public List<Ant> getAnts() { return ants; }
    public List<Ladybug> getLadybugs() { return ladybugs; }
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
     * {@summary Let ladybugs eat aphids.}
     * 
     * @return true if a ladybug have interact
     */
    public boolean ladybugEat() {
        boolean haveInteract = false;
        for (Ladybug ladybug : ladybugs) {
            Set<Aphid> eated = new HashSet<Aphid>();
            for (Aphid aphid : aphids) {
                if (ladybug.hitBoxConnected(aphid)) {
                    haveInteract = true;
                    eated.add(aphid);
                    // ladybug.addScorePoints(aphid.getGivenPoints());
                    getPlayer(0).addScore(aphid.getGivenPoints());
                    // System.out.println("Eating " + aphid);
                }
            }
            aphids.removeAll(eated);
            for (Aphid aphid : eated) {
                aphid.removeActor();
            }
        }
        return haveInteract;
    }

    /**
     * @return All creatures: aphids + ants + ladybugs
     */
    public Iterable<Creature> allCreatures() {
        List<Creature> l = new LinkedList<Creature>();
        l.addAll(aphids);
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
    public Iterable<Actor> allActors() {
        List<Actor> l = new LinkedList<Actor>();
        if (mapActorBg != null) {
            l.add(mapActorBg);
        }
        for (Creature creature : allCreatures()) {
            if (creature.getActor() != null) {
                l.add(creature.getActor());
            }
        }
        if (mapActorFg != null) {
            l.add(mapActorFg);
        }
        return l;
    }
    /**
     * {@summary Update actor visibility so that the only visible actor are the one close enoth to the player.}
     * 
     * @param playerId current player id
     */
    public void updateActorVisibility(int playerId) {
        Creature playedCreature = getPlayerCreature(playerId);
        if (playedCreature != null) {
            for (Creature creature : allCreatures()) {
                creature.getActor().setVisible(playedCreature.see(creature));
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

    public boolean isAllAphidGone() { return aphids.size() == 0; }

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
        private int maxScore;
        private GameState gs;

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

            if (maxScore < 1) {
                maxScore = 1;
            }
            gs.setMaxScore(maxScore);

            // initialize default game
            addMapBackground();
            // TODO move to the builder parameter
            addCreatures(maxScore, 1, 0);
            gs.players.add(new Player(gs.ladybugs.get(0)));
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
        public GameStateBuilder setMaxScore(int maxScore) {
            this.maxScore = maxScore;
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
        private void addCreatures(int aphidsNumber, int ladybugsNumber, int antsNumber) {
            addC(aphidsNumber, 0.1f, 0.2f, true, true, Aphid.class);
            addC(ladybugsNumber, 0.4f, 0.4f, true, false, Ladybug.class);
            addC(antsNumber, 0.3f, 0.35f, true, true, Ant.class);
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
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    // } catch (IllegalArgumentException | SecurityException | NullPointerException e) {
                    System.out.println("Fail to add a new Creature");
                    e.printStackTrace();
                }
            }
        }
        /**
         * {@summary Add the map background.}
         */
        private void addMapBackground() {
            gs.mapActorBg = new MapActor(Math.max(1, mapWidth), Math.max(1, mapHeight),
                    new com.badlogic.gdx.graphics.Color(8 / 255f, 194 / 255f, 0 / 255f, 1f));
            // gs.mapActorBg = new MapActor(Math.max(1, mapWidth), Math.max(1, mapHeight),
            // Color.OLIVE);
        }
        /**
         * {@summary Add the map foreground as a black mask.}
         */
        private void addMapForeground() {
            gs.mapActorFg = new MapActor(Math.max(1, mapWidth * 2), Math.max(1, mapHeight * 2), Color.BLACK);
            gs.mapActorFg.addToExclude(0f, 0f, gs.getPlayerCreature(0).getVisionRadius());
        }
    }
}

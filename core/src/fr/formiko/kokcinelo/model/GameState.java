package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.view.MapActor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameState {
    private List<Aphid> aphids;
    private List<Ant> ants;
    private List<Ladybug> ladybugs;
    private List<Player> players;
    private MapActor mapActorBg;
    private MapActor mapActorFg;

    public MapActor getMapActorFg() {
        return mapActorFg;
    }

    public MapActor getMapActor() {
        return mapActorBg;
    }

    private GameState() {
        aphids = new ArrayList<Aphid>();
        ants = new ArrayList<Ant>();
        ladybugs = new ArrayList<Ladybug>();
        players = new ArrayList<Player>();
    }

    public Player getPlayer(int playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }

    public Creature getPlayerCreature(int playerId) {
        Player p = getPlayer(playerId);
        if (p != null) {
            return p.getPlayedCreature();
        }
        return null;
    }

    public float getMapWidth() {
        return getMapActor().getWidth();
    }

    public float getMapHeight() {
        return getMapActor().getHeight();
    }

    public void addCreature(Creature c) {
        if (c instanceof Aphid) {
            aphids.add((Aphid) c);
        } else if (c instanceof Ant) {
            ants.add((Ant) c);
        } else if (c instanceof Ladybug) {
            ladybugs.add((Ladybug) c);
        }
    }

    public void interact() {
        for (Ladybug ladybug : ladybugs) {
            Set<Aphid> eated = new HashSet<Aphid>();
            for (Aphid aphid : aphids) {
                if (ladybug.hitBoxConnected(aphid)) {
                    eated.add(aphid);
                    ladybug.addScorePoints(aphid.getGivenPoints());
                }
            }
            aphids.removeAll(eated);
        }
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
    public Iterable<Actor> allActors() {
        List<Actor> l = new LinkedList<Actor>();
        if(mapActorBg!=null){
            l.add(mapActorBg);
        }
        for (Creature creature : allCreatures()) {
            if(creature.getActor()!=null){
                l.add(creature.getActor());
            }
        }
        if(mapActorFg!=null){
            l.add(mapActorFg);
        }
        return l;
    }

    public void updateActorVisibility(int playerId) {
        Creature playedCreature = getPlayerCreature(playerId);
        if (playedCreature != null) {
            for (Creature creature : allCreatures()) {
                creature.getActor().setVisible(playedCreature.see(creature));
            }
        }
    }

    @Override
    public String toString() {
        return "GameState [mapCoordinate=" + mapActorBg + ", aphids=" + aphids + ", ants=" + ants + ", ladybugs="
                + ladybugs + ", players=" + players + "]"
                + " " + mapActorBg + " " + mapActorFg;
    }

    // static
    public static GameStateBuilder builder() {
        return new GameStateBuilder();
    }

    public static class GameStateBuilder {
        private int mapWidth;
        private int mapHeight;
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

            // initialize default game
            addMapBackground();
            // TODO move to the builder parameter
            addCreatures(200, 1, 0);
            gs.players.add(new Player(gs.ladybugs.get(0)));
            // addMapForeground();

            // System.out.println(gs);
            return gs;
        }

        public GameStateBuilder setMapWidth(int mapWidth) {
            this.mapWidth = mapWidth;
            return this;
        }

        public GameStateBuilder setMapHeight(int mapHeight) {
            this.mapHeight = mapHeight;
            return this;
        }

        private void addCreatures(int aphidsNumber, int ladybugNumber, int antNumber) {
            addC(aphidsNumber, 0.1f, 0.2f, true, true, Aphid.class);
            addC(ladybugNumber, 0.4f, 0.4f, false, false, Ladybug.class);
            addC(antNumber, 0.3f, 0.35f, true, true, Ant.class);
        }

        private void addC(final int numberToAdd, final float zoomMin, final float zoomMax,
                final boolean randomLocaction, final boolean randomRotation,
                final Class<? extends Creature> creatureClass) {
            for (int i = 0; i < numberToAdd; i++) {
                try {
                    Creature c = creatureClass.getDeclaredConstructor().newInstance();
                    if (randomLocaction) {
                        c.getActor().setRandomLoaction(gs.getMapWidth(), gs.getMapHeight());
                    }
                    if (randomRotation) {
                        c.getActor().setRotation(Controller.getRandom().nextFloat(360f));
                    }
                    if (zoomMin > 0) {
                        if (zoomMax > zoomMin) {
                            c.getActor().setZoom(zoomMin + Controller.getRandom().nextFloat(zoomMax - zoomMin));
                        } else {
                            c.getActor().setZoom(zoomMin);
                        }
                    }
                    gs.addCreature(c);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    System.out.println("Fail to add a new Creature");
                    e.printStackTrace();
                }
            }
        }

        private void addMapBackground() {
            gs.mapActorBg = new MapActor(Math.max(1, mapWidth), Math.max(1, mapHeight), new com.badlogic.gdx.graphics.Color(8/255f, 194/255f, 0/255f, 1f));
            // gs.mapActorBg = new MapActor(Math.max(1, mapWidth), Math.max(1, mapHeight), Color.OLIVE);
        }

        private void addMapForeground() {
            gs.mapActorFg = new MapActor(Math.max(1, mapWidth), Math.max(1, mapHeight), Color.BLACK);
        }
    }
}

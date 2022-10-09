package fr.formiko.kokcinelo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Rectangle;

public class GameState {
    private Rectangle mapCoordinate;
    private List<Aphid> aphids;
    private List<Ant> ants;
    private List<Ladybug> ladybugs;
    private List<Player> players;

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

    public Set<Creature> getCreatureToPrint(int playerId) {
        Set<Creature> toPrint = new HashSet<Creature>();
        Player p = getPlayer(playerId);
        Creature playedCreature = p.getPlayedCreature();
        if (p != null) {
            for (Creature creature : allCreatures()) {
                if (playedCreature.see(creature)) {
                    toPrint.add(creature);
                }
            }
        }
        return toPrint;
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

    //static
    public static GameStateBuilder builder() {
        return new GameStateBuilder();
    }

    public static class GameStateBuilder {
        private int mapWidth;
        private int mapHeight;

        private GameStateBuilder() {
        }

        public GameState build() {
            GameState gs = new GameState();
            gs.mapCoordinate = new Rectangle(0, 0, Math.max(1, mapWidth), Math.max(1, mapHeight));
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
    }
}

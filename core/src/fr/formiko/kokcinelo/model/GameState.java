package fr.formiko.kokcinelo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
        if (p != null) {
            Creature playedCreature = p.getPlayedCreature();
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
        private GameState gs;

        private GameStateBuilder() {
        }

        public GameState build() {
            gs = new GameState();
            gs.mapCoordinate = new Rectangle(0, 0, Math.max(1, mapWidth), Math.max(1, mapHeight));

            //initialize default game
            //TODO change to the builder parameter
            addAphids(20);
            Ladybug lb = new Ladybug();
            lb.getActor().setWidth(Gdx.graphics.getWidth() / 10);
            lb.getActor().setHeight((lb.getActor().getWidth() * lb.getActor().getHeight()) / lb.getActor().getWidth());
            gs.ladybugs.add(lb);
            gs.players.add(new Player(lb));

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

        private void addAphids(int numberToAdd) {
            Random ran = new Random();
            for (int i = 0; i < numberToAdd; i++) {
                Aphid a = new Aphid();
                Rectangle aphid = new Rectangle();
                aphid.width = Gdx.graphics.getWidth() / 50;
                // keep racio
                aphid.height = (aphid.width * a.getActor().getHeight()) / a.getActor().getWidth();
                // random location
                aphid.x = ran.nextInt((int) (Gdx.graphics.getWidth() - aphid.width));
                aphid.y = ran.nextInt((int) (Gdx.graphics.getHeight() - aphid.width));
                a.getActor().setBounds(aphid.getX(), aphid.getY(), aphid.getWidth(), aphid.getHeight());
                gs.aphids.add(a);
            }
        }
    }
}

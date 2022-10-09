package fr.formiko.kokcinelo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Rectangle;

public class GameState {
    private Rectangle mapCoordinate;
    private List<Aphid> aphids;
    private List<Ant> ants;
    private List<Ladybug> ladybugs;

    private GameState() {
        aphids = new ArrayList<Aphid>();
        ants = new ArrayList<Ant>();
        ladybugs = new ArrayList<Ladybug>();
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
                if (ladybug.hitBixConnected(aphid)) {
                    eated.add(aphid);
                    ladybug.addScorePoints(aphid.getGivenPoints());
                }
            }
            aphids.removeAll(eated);
        }
    }

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

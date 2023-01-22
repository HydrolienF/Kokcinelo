package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.tools.Files;
import fr.formiko.kokcinelo.tools.Musics;
import fr.formiko.kokcinelo.view.GameScreen;
import fr.formiko.kokcinelo.view.MenuScreen;
import fr.formiko.kokcinelo.view.VideoScreen;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * {@summary Controller is the link between View &#38; model.}
 * Because of Seen2D Actor, there is some view item in the model.
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.1
 */
public class Controller {
    private GameState gs;
    private App app;
    private boolean spectatorMode;
    private String levelId;

    private static Controller controller;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     * 
     * @param app app to send action to
     */
    public Controller(App app) {
        this.app = app;
        controller = this;
        App.log(0, "constructor", "new Controller: " + toString());
    }

    // GET SET -------------------------------------------------------------------
    public static Controller getController() { return controller; }
    public static void setController(Controller controller) { Controller.controller = controller; }
    public GameScreen getGameScreen() { return (GameScreen) getScreen(); }
    public Screen getScreen() { return app.getScreen(); }
    public void setScreen(Screen screen) { app.setScreen(screen); }
    public int getLocalPlayerId() { return gs.getLocalPlayerId(); }
    public boolean isSpectatorMode() { return spectatorMode; }
    public void setSpectatorMode(boolean spectatorMode) { this.spectatorMode = spectatorMode; }
    public int getNumberOfAphids() { return gs.getAphids().size(); }

    // FUNCTIONS -----------------------------------------------------------------

    public void startApp() { createNewMenuScreen(); }

    private void createNewVideoScreen() { setScreen(new VideoScreen(levelId)); }

    public void createNewMenuScreen() {
        setScreen(new MenuScreen());
        Musics.playMenuMusic();
    }
    /**
     * {@summary End the current screen.}
     * Current screen is supposed to be a MenuScreen. Other wise it will do nothing.
     */
    public synchronized void endMenuScreen() {
        if (getScreen() != null && getScreen() instanceof MenuScreen) {
            Screen toDispose = getScreen();
            levelId = ((MenuScreen) (getScreen())).getLevelId();
            createNewVideoScreen();
            toDispose.dispose();
        } else {
            App.log(0, "", "getScreen() is not a MenuScreen");
        }
    }
    /**
     * {@summary End the current screen.}
     * Current screen is supposed to be a VideoScreen. Other wise it will do nothing.
     */
    public synchronized void endVideoScreen() {
        if (getScreen() != null && getScreen() instanceof VideoScreen) {
            Screen toDispose = getScreen();
            createNewGame();
            toDispose.dispose();
        } else {
            App.log(0, "", "getScreen() is not a VideoScreen");
        }
    }

    /**
     * {@summary End the current screen.}
     * Current screen is supposed to be a GameScreen. Other wise it will do nothing.
     */
    public synchronized void endGameScreen() {
        if (getScreen() != null && getScreen() instanceof GameScreen) {
            Screen toDispose = getScreen();
            createNewMenuScreen();
            toDispose.dispose();
        } else {
            App.log(0, "", "getScreen() is not a GameScreen");
        }
    }

    /**
     * {@summary Update zoom of camera.}
     * 
     * @param amountY zoom amount
     */
    public void addZoom(float amountY) {
        amountY *= -1;
        // getCamera().zoom += amountY * 0.05f;
        getCamera().zoom *= (1 - amountY / 20);
        if (getCamera().zoom < 0.1f) {
            getCamera().zoom = 0.1f;
        }
    }

    /**
     * {@summary Move the player Creature &#38; synchonize Camera and masks.}
     * 
     * @param playerId id of the player to move
     */
    public void movePlayer(int playerId) {
        Creature c = gs.getPlayerCreature(playerId);
        c.moveFront();
        c.moveIn(gs.getMapWidth(), gs.getMapHeight());
        // synchonize things that depend of c position
        synchronizeCamera(c);

        if (gs.getMapActorFg() != null) {
            gs.getMapActorFg().setX(c.getCenterX() - gs.getMapActorFg().getWidth() / 2);
            gs.getMapActorFg().setY(c.getCenterY() - gs.getMapActorFg().getHeight() / 2);
            gs.getMapActorFg().setVisible(!isSpectatorMode());
        }
        c.goTo(getVectorStageCoordinates(Gdx.input.getX(), Gdx.input.getY()));
    }

    /**
     * {@summary Move aphids.}
     * Aphids first run away from the closest ladybug they can see if they can see one.
     * Else they move slowly to a random direction &#38; some time change it.
     * If they hit a wall, they change there wanted rotation angle for the nexts turns.
     */
    public void moveAphids() {
        for (Aphid aphid : gs.getAphids()) {
            Ladybug ladybug = aphid.closestLadybug(gs.getLadybugs());
            if (ladybug != null) {
                // Run away move
                aphid.runAwayFrom(new Vector2(ladybug.getCenterX(), ladybug.getCenterY()));
                aphid.moveFront();
            } else {
                // Normal move
                double r = Math.random() / (Gdx.graphics.getDeltaTime() * 100);
                if (r < 0.02) { // randomize rotation
                    aphid.setWantedRotation((float) (Math.random() * 40) - 20f);
                }
                aphid.moveFront(0.3f);
            }
            // if have been move to avoid wall
            if (aphid.moveIn(gs.getMapWidth(), gs.getMapHeight())) {
                if (aphid.getWantedRotation() == 0f) { // if have not already choose a new angle to get out.
                    aphid.setWantedRotation((160f + (float) (Math.random() * 40)) % 360f);
                }
            }
        }
    }

    /**
     * {@summary Synchronize the camera with the given Creature.}
     * 
     * @param c creature to synchronize with
     */
    public void synchronizeCamera(Creature c) {
        getCamera().position.x = c.getCenterX();
        getCamera().position.y = c.getCenterY();
    }
    /**
     * {@summary Create a new Game.}
     * Create the GameState with Game data.
     * Set current Screen as a new GameScreen.
     */
    private void createNewGame() {
        App.log(0, "Need to start new Game");
        int gameTime = 60;
        setSpectatorMode(false);
        Musics.setMusic("Waltz of the Night 1min");
        gs = GameState.builder().setMaxScore(100).setMapHeight(2000).setMapWidth(2000).setLevelId(levelId).build();
        app.setScreen(new GameScreen(app));
        Musics.play();
        App.log(1, "start new Game");
        // app.getGameMusic().setPosition(178.1f - gameTime); // end at 178
        getGameScreen().resume();
        getGameScreen().setGameTime(gameTime);
        App.log(1, "new Game started");
    }
    public void restartGame() { createNewGame(); }
    public void updateActorVisibility(int playerId) { gs.updateActorVisibility(playerId, spectatorMode); }
    public Iterable<Creature> allCreatures() { return gs.allCreatures(); }
    public Iterable<Actor> allActors() { return gs.allActors(); }
    public boolean isAllAphidGone() { return gs.isAllAphidGone(); }
    // public void removeActorFromStage(Actor actor) { actor.remove(); }

    /**
     * {@summary Let Creature interact with each other.}
     * It let ladybugs eat aphids and if they do, update player score &#38; play matching sound.
     */
    public void interact() {
        if (gs.ladybugEat()) {
            getGameScreen().setPlayerScore(gs.getScore());
            app.playEatingSound();
        }
    }
    /**
     * {@summary End game by launching sound &#38; end game menu.}
     */
    public void gameOver() {
        App.log(1, "gameOver");
        if (getGameScreen().isStop()) {
            return;
        }
        // Musics.dispose();
        setSpectatorMode(true);
        getGameScreen().stopAfterNextDraw();
        boolean haveWin = gs.getScore() == gs.getMaxScore();
        // boolean haveWin = gs.getScore() >= gs.getMaxScore() / 2;
        app.playEndGameSound(haveWin);
        getGameScreen().createEndGameMenu(gs.getScore(), gs.getMaxScore(), haveWin);
        saveScoreInFile();
        // TODO it may be a better id to save score only & find if level are unlock from score file.
        if (haveWin) { // TODO & level haven't been unlock yet.
            unlockNextLevel(levelId);
        }
    }

    /**
     * {@summary Pause game or resume depening of current state.}
     * It pause move of creature & music.
     */
    public void pauseResume() {
        if (getGameScreen().isPause()) {
            getGameScreen().resume();
            Musics.play();
        } else {
            getGameScreen().pause();
            Musics.pause();
        }
    }

    public void dispose() { app.dispose(); }


    // Files ----------------------------------------------------------------------------------------------
    /**
     * {@summary Save a string in a file.}
     * 
     * @param fileName       name of the file
     * @param contentToWrite content to write into the file
     * @param append         if true, append the content to the file, else overwrite the content of the file
     */
    public void saveStringInFile(String fileName, String contentToWrite, boolean append) {
        App.log(0, "FILES", "Save \"" + contentToWrite + "\" into " + fileName + " append=" + append);
        FileHandle file = Gdx.files.absolute(Files.getDataPath() + fileName);
        file.writeString(contentToWrite, append);
    }
    /**
     * {@summary Read a string from a file.}
     * 
     * @param fileName name of the file
     * @return content of the file
     */
    public String readStringInFile(String fileName) {
        App.log(0, "FILES", "Read the content of " + fileName);
        FileHandle file = Gdx.files.absolute(Files.getDataPath() + fileName);
        if (file.exists()) {
            return file.readString();
        } else {
            return null;
        }
    }
    private static String getScoresFileName() { return "scores.csv"; }
    private static String getUnlockedLevelsFileName() { return "unlockedLevels.csv"; }
    /**
     * {@summary Save the score of the current game.}
     * It save the levelId, the score and the time of the game.
     */
    public void saveScoreInFile() {
        saveStringInFile(getScoresFileName(), gs.getLevelId() + "," + gs.getPercentScore() + "," + System.currentTimeMillis() + "\n", true);
    }
    /**
     * {@summary Save all important data.}
     */
    public void saveData() {
        Map<String, String> map = App.getDataMap();
        map.put("lastDatePlayed", System.currentTimeMillis() + "");
        long toAddTimePlayed = System.currentTimeMillis() - Long.parseLong(map.get("startPlaying"));
        App.log(1, "FILES", "Added time played: " + toAddTimePlayed);
        long timePlayed;
        if (map.containsKey("timePlayed")) {
            timePlayed = Long.parseLong(map.get("timePlayed"));
        } else {
            timePlayed = 0;
        }
        map.put("timePlayed", "" + (timePlayed + toAddTimePlayed));
        Files.saveInFile("data.yml", map);
        App.log(1, "FILES", "Saved data: " + map);
    }
    /**
     * {@summary Load all important data.}
     */
    public Map<String, String> loadData() {
        Map<String, String> map;
        try {
            map = Files.loadFromFile("data.yml");
        } catch (Exception e) {
            map = new HashMap<String, String>();
            map.put("firstDatePlayed", System.currentTimeMillis() + "");
            String l = Locale.getDefault().getLanguage();
            if (!App.SUPPORTED_LANGUAGE.contains(l)) {
                l = "en";
            }
            map.put("language", l);
        }
        map.put("startPlaying", System.currentTimeMillis() + "");
        App.log(1, "FILES", "Loaded data: " + map);
        return map;
    }
    /**
     * {@summary Return the best score of a level.}
     * 
     * @param levelId id of the level
     * @return best score of the level
     */
    public int getBestScore(String levelId) {
        String scores = readStringInFile(getScoresFileName());
        if (scores == null) {
            return 0;
        }
        int max = 0;
        for (String line : scores.split("\n")) {
            String[] data = line.split(",");
            if (data[0].equals(levelId)) {
                max = Math.max(max, Integer.parseInt(data[1]));
            }
        }
        return max;
    }
    /**
     * {@summary Return the last score of a level.}
     * 
     * @param levelId id of the level
     * @return last score of the level
     */
    public int getLastScore(String levelId) {
        String scores = readStringInFile(getScoresFileName());
        if (scores == null) {
            return 0;
        }
        int score = 0;
        long maxTime = 0;
        for (String line : scores.split("\n")) {
            String[] data = line.split(",");
            if (data[0].equals(levelId)) {
                long time = Long.parseLong(data[2]);
                if (maxTime < time) {
                    maxTime = time;
                    score = Integer.parseInt(data[1]);
                }
            }
        }
        return score;
    }
    /**
     * {@summary Load the unlocked levels.}
     */
    public Set<String> loadUnlockedLevels() {
        HashSet<String> unlockedLevels = new HashSet<String>();
        String s = readStringInFile(getUnlockedLevelsFileName());
        unlockedLevels.add("1K");
        if (s == null || s.equals("")) {
            unlockLevel("1K");
        } else {
            String[] data = s.split(",");
            for (String string : data) {
                if (!string.equals("")) {
                    unlockedLevels.add(string);
                }
            }
        }
        App.log(1, "Unlocked levels: " + unlockedLevels);
        return unlockedLevels;
    }
    /**
     * {@summary Save new unlocked levels.}
     */
    public void unlockNextLevel(String levelId) {
        if (levelId.equals("1K")) {
            unlockLevel("2K");
            unlockLevel("2F");
        } else {
            unlockLevel((levelId.charAt(0) + 1) + levelId.substring(1, 2));
        }
    }
    /**
     * {@summary Save a new unlocked level.}
     */
    public void unlockLevel(String levelId) { saveStringInFile(getUnlockedLevelsFileName(), "," + levelId, true); }


    /**
     * {@summary Return current used camera.}
     * 
     * @return current used camera
     */
    private OrthographicCamera getCamera() {
        // return gScreen.camera;
        return GameScreen.getCamera();
    }
    /**
     * {@summary Return a vector of coordinate from screen x, y to stage x, y.}
     * 
     * @param x x screen coordinate
     * @param y y screen coordinate
     * @return Vector of coordinate from screen x, y to stage x, y
     */
    private Vector2 getVectorStageCoordinates(float x, float y) {
        return getGameScreen().getStage().screenToStageCoordinates(new Vector2(x, y));
    }
}

package fr.formiko.kokcinelo;

import fr.formiko.usual.color;// HTML INCOMPATIBLE
import java.text.SimpleDateFormat;
import java.util.Date;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * {@summary Main class that represent the App &#38; call other Screens.}
 * It call MainMenuScreen
 * 
 * @see com.badlogic.gdx.Game
 * @author Hydrolien
 * @version 0.2
 * @since 0.1
 */
public class App extends Game {
    public SpriteBatch batch;
    private Sound eatingSound;
    private Music gameMusic;

    private String[] args;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // HTML INCOMPATIBLE
    private int logLevel = Application.LOG_INFO;
    private static boolean launchFromLauncher;

    public App(String[] args) { this.args = args; }
    public App() { this(null); }

    public Music getGameMusic() { return gameMusic; }

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Main function call when App is created.}
     * It call MainMenuScreen.
     * 
     * @see com.badlogic.gdx.ApplicationListener#create()
     */
    @Override
    public void create() {
        setOptionsFromArgs();
        // color.iniColor();//HTML INCOMPATIBLE
        Gdx.app.setLogLevel(logLevel);
        App.log(1, "APP", "Start app");
        batch = new SpriteBatch();
        // TODO add MainMenuScreen that will be call 1st & call GameMenu after
        startNewGame();
    }
    /**
     * {@summary Start a new Game on this app in GUI.}
     */
    public void startNewGame() {
        Controller c = new Controller(this);
        Controller.setController(c);
        c.createNewGame();
        // c.createNewVideoScreen();
    }

    /***
     * {@summary call render function of sub screens}
     * 
     * @see com.badlogic.gdx.Game#render()
     */
    @Override
    public void render() { super.render(); }

    /**
     * {@summary call dispose function of item that need to be dispose so that App close itself.}
     * 
     * @see com.badlogic.gdx.Game#dispose()
     */
    @Override
    public void dispose() {
        // TODO be able to dispose even for html version
        // System.exit(0); //HTML INCOMPATIBLE
        Gdx.app.exit();
    }

    // music --------------------------------------------------------------------------------------
    /**
     * {@summary Play the game music.}
     */
    public void createGameMusic() {
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/Waltz of the Night 1min.mp3"));
        // gameMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/Waltz of the Night shorted.mp3"));
    }
    /**
     * {@summary Play the eating sound.}
     * Sound can be play many times &#38; at same time.
     */
    public void playEatingSound() {
        if (eatingSound == null) {
            eatingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crock.mp3"));
        }
        eatingSound.play();
    }
    /**
     * {@summary Play the end game sound.}
     * Sound can be play many times &#38; at same time.
     * 
     * @param haveWin the sound is different if game is win or lost
     */
    public void playEndGameSound(boolean haveWin) {
        String fileName;
        if (haveWin) {
            fileName = "win";
        } else {
            fileName = "lose";
        }
        Sound s = Gdx.audio.newSound(Gdx.files.internal("sounds/" + fileName + ".mp3"));
        gameMusic.stop();
        s.play();
    }

    // LOGS -------------------------------------------------------------------
    /**
     * {@summary Save or print log using Gdx.app settings.}
     * 
     * @param logLevel  int value from 0 (debug) to 4 (error) to represent gravity of log
     * @param tag       tag of the log
     * @param message   message of the log
     * @param exception exception that have been catch
     */
    public static void log(int logLevel, String tag, String message, Exception exception) {
        // tag = updatedTag(logLevel, tag);
        String tagTemp = getCurrentTime() + " " + logLevelToString(logLevel);
        // tagTemp+= " " + Thread.currentThread().getName(); //HTML INCOMPATIBLE
        if (tag == null || tag.equals("")) {
            tag = tagTemp;
        } else {
            tag = tagTemp + " " + tag;
        }
        if (!launchFromLauncher) {
            tag = logLevelToStringColor(logLevel) + tag;
            tag += color.NEUTRAL;// HTML INCOMPATIBLE
        }
        if (Gdx.app != null) {
            switch (logLevel) {
            case 0:
                Gdx.app.debug(tag, message);
                break;
            case 1:
                Gdx.app.log(tag, message);
                break;
            // case 2, 3, 4: //HTML INCOMPATIBLE
            case 2:
            case 3:
            case 4:
                if (exception != null) {
                    Gdx.app.error(tag, message, exception);
                } else {
                    Gdx.app.error(tag, message);
                }
                if (logLevel == 4) {
                    Controller.getController().dispose();
                }
                break;
            }
        } else {
            System.out.println("[" + tag + "] " + message);
        }
    }
    /***
     * {@summary Save or print log using Gdx.app settings.}
     * 
     * @param logLevel int value from 0 (debug) to 4 (error) to represent gravity of log
     * @param tag      tag of the log
     * @param message  message of the log
     */
    public static void log(int logLevel, String tag, String message) { log(logLevel, tag, message, null); }
    /***
     * {@summary Save or print log using Gdx.app settings.}
     * 
     * @param logLevel int value from 0 (debug) to 4 (error) to represent gravity of log
     * @param message  message of the log
     */
    public static void log(int logLevel, String message) { log(logLevel, null, message); }
    /***
     * {@summary Save or print log using Gdx.app settings.}
     * 
     * @param message message of the log
     */
    public static void log(String message) { log(0, message); }
    /**
     * {@summary Return log level String.}
     * 
     * @param logLevel int value from 0 (debug) to 4 (error) to represent gravity of log
     * @return log level String
     */
    private static String logLevelToString(int logLevel) {
        switch (logLevel) {
        case 0:
            return "DEBUG";
        case 1:
            return "INFO";
        case 2:
            return "WARNING";
        case 3:
            return "ERROR";
        case 4:
            return "FATAL ERROR";
        default:
            return "NULL";
        }
    }
    /**
     * {@summary Return log level String color.}
     * 
     * @param logLevel int value from 0 (debug) to 4 (error) to represent gravity of log
     * @return log level String color
     */
    private static String logLevelToStringColor(int logLevel) {
        // HTML INCOMPATIBLE
        switch (logLevel) {
        case 0:
            return color.BROWN;
        case 1:
            return color.BLUE;
        case 2:
            return color.YELLOW;
        case 3:
            return color.RED;
        case 4:
            return color.RED;
        default:
            return color.NEUTRAL;
        }
        // return "";
    }
    /***
     * {@summary Return current time in standardized format.}
     * 
     * @return current time in standardized format
     */
    public static String getCurrentTime() {
        return formatter.format(new Date(System.currentTimeMillis()));// HTML INCOMPATIBLE
        // return "";
    }


    private void setOptionsFromArgs() {
        if (args == null) {
            return;
        }
        for (String arg : args) {
            while (arg != null && arg.length() > 1 && arg.charAt(0) == '-') {
                arg = arg.substring(1);
            }
            switch (arg) {
            // case "version", "v": { //HTML INCOMPATIBLE
            case "version":
            case "v": {
                FileHandle versionFile = Gdx.files.internal("version.md");
                System.out.println(versionFile.readString());
                // System.exit(0); //HTML INCOMPATIBLE
                Gdx.app.exit();
                break;
            }
            // case "quiet", "q": { //HTML INCOMPATIBLE
            case "quiet":
            case "q": {
                logLevel = Application.LOG_NONE;
                break;
            }
            case "verbose": {
                logLevel = Application.LOG_DEBUG;
                break;
            }
            case "launchFromLauncher": {
                launchFromLauncher = true;
                logLevel = Application.LOG_DEBUG; // because all logs go to a file, it's better to have them all.
                break;
            }
            default: {
                log(2, arg + " don't match any args possible");
            }
            }
        }
    }
}

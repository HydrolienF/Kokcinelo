package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.tools.Files;
import fr.formiko.kokcinelo.tools.Musics;
import fr.formiko.usual.color;// HTML INCOMPATIBLE
import fr.formiko.usual.g;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * {@summary Main class that represent the App &#38; call other Screens.}
 * It call MainMenuScreen
 * 
 * @see com.badlogic.gdx.Game
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class App extends Game {
    public SpriteBatch batch;
    private static Map<String, Sound> soundMap = new HashMap<String, Sound>();

    private static Map<String, String> data;

    private String[] args;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // HTML INCOMPATIBLE
    private int logLevel = Application.LOG_INFO;
    private static boolean launchFromLauncher;
    public static final List<String> PLAYABLE_LEVELS = List.of("1K", "2K", "2F", "3K", "3F");
    // public static final List<String> PLAYABLE_LEVELS = List.of("1K", "2K", "3K", "4F", "2F", "3F");
    public static List<String> SUPPORTED_LANGUAGES;
    public static Map<String, String> LANGUAGES_NAMES;
    public static Map<String, Integer> LANGUAGES_PERCENTAGES;
    private static boolean withCloseButton;

    public static final List<Integer> STARS_SCORES = List.of(50, 80, 100);
    public static final Color BLUE_BACKGROUND = new Color(0, 203f / 255, 1, 1);
    public static final Color GREEN = new Color(8 / 255f, 194 / 255f, 0 / 255f, 1f);
    private final Native nativ;


    public App(String[] args, Native nativ) {
        this.args = args;
        this.nativ = nativ;
    }
    public App() { this(null, new NullNative()); }


    public static boolean isWithCloseButton() { return withCloseButton; }
    public static void setWithCloseButton(boolean withCloseButton) { App.withCloseButton = withCloseButton; }
    public static Map<String, String> getDataMap() { return data; }
    public static String getLanguage() { return data.get("language"); }
    /**
     * {@summary Set language & update translation list.}
     * 
     * @param language language to switch to
     */
    public static void setLanguage(String language) {
        if (language != null) {
            data.put("language", language);
        }
        updateLanguage();
    }
    /**
     * {@summary Update translation list.}
     */
    private static void updateLanguage() {
        HashMap<String, String> defaultMap = Files.getText("en");
        HashMap<String, String> userLanguage = Files.getText(getLanguage());
        for (String key : userLanguage.keySet()) {
            if (userLanguage.get(key) != null) {
                defaultMap.put(key, userLanguage.get(key));
            }
        }
        // App.log(1, "map: " + userLanguage);
        g.setMap(defaultMap);
        App.log(1, "Current language: " + getLanguage());
    }


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
        Controller.setController(new Controller(this));

        // if (Os.getOs().isWindows()) {
        // nativ.toFront(); //not working better.
        // }

        loadLanguagesData();
        data = Controller.getController().loadData();
        updateLanguage();
        // // full screen
        // try {
        // Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        // Gdx.graphics.setFullscreenMode(currentMode);
        // } catch (Exception e) {
        // Gdx.app.log("Init", "Fail to set full screen");
        // }
        color.iniColor();// HTML INCOMPATIBLE
        Gdx.app.setLogLevel(logLevel);
        App.log(1, "APP", "Start app");
        batch = new SpriteBatch();
        startNewGame();
    }
    /**
     * {@summary Start a new Game on this app in GUI.}
     */
    public void startNewGame() {
        // App.log(1, "SCORE", "Current best score " + Controller.getController().getBestScore("1K"));
        // App.log(1, "SCORE", "Last score " + Controller.getController().getLastScore("1K"));
        Controller.getController().startApp();
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
        // Save played time etc
        Controller.getController().saveData();
        App.log(1, "Normal close of the app.");
        Gdx.app.exit();
    }

    public static boolean isPlayableLevel(String levelId) { return PLAYABLE_LEVELS.contains(levelId); }

    // music --------------------------------------------------------------------------------------
    /**
     * {@summary Play the given sound.}
     * Sound can be play many times &#38; at same time.
     * 
     * @param fileName name of the sound file
     * @param volume   volume of the sound in [0, 1]
     * @param pan      left rigth ballance of the sound file in [-1, 1]
     */
    public static void playSound(String fileName, float volume, float pan) {
        if (soundMap.get(fileName) == null) {
            soundMap.put(fileName, Gdx.audio.newSound(Gdx.files.internal("sounds/" + fileName + ".mp3")));
        }
        soundMap.get(fileName).play(volume, 1f, pan);
    }
    /**
     * {@summary Play the given sound with default volume &#38; default pan.}
     * Sound can be play many times &#38; at same time.
     * 
     * @param fileName name of the sound file
     */
    public static void playSound(String fileName) { playSound(fileName, 1f, 0f); }
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
        Musics.play(fileName);
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
    /**
     * {@summary Return current time in standardized format.}
     * 
     * @return current time in standardized format
     */
    public static String getCurrentTime() {
        return formatter.format(new Date(System.currentTimeMillis()));// HTML INCOMPATIBLE
        // return "";
    }

    /**
     * {@summary Update some option from command line args.}
     */
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
    /**
     * {@summary Load languages data from files.}
     */
    private static void loadLanguagesData() {
        App.log(1, "Start loadLanguagesData()");
        LANGUAGES_NAMES = Files.loadMapFromCSVFile("languages/languagesList.csv", true);
        // App.log(1, "" + App.LANGUAGES_NAMES);
        loadListOfSupportedLanguages();
        App.log(1, "End loadLanguagesData()");
    }
    /**
     * @return The list of supported languages
     */
    private static void loadListOfSupportedLanguages() {
        SUPPORTED_LANGUAGES = new ArrayList<String>();
        Map<String, String> tempMap = Files.loadMapFromCSVFile("languages/languagesPercents.csv", true);
        // It may be useful for performance issues, some day, to load languages percents from a single file include in packaged jar
        if (tempMap.isEmpty()) {
            calculateLanguagesPercentages();
        } else { // if it exist in a single file
            for (String key : tempMap.keySet()) {
                LANGUAGES_PERCENTAGES.put(key, Integer.parseInt(tempMap.get(key)));
            }
        }
        for (String key : LANGUAGES_PERCENTAGES.keySet()) {
            SUPPORTED_LANGUAGES.add(key);
        }
        SUPPORTED_LANGUAGES.sort((String s1, String s2) -> LANGUAGES_PERCENTAGES.get(s2).compareTo(LANGUAGES_PERCENTAGES.get(s1)));
    }

    /**
     * {@summary Calculate languages percentages from all translations maps.}
     */
    private static void calculateLanguagesPercentages() {
        LANGUAGES_PERCENTAGES = new HashMap<String, Integer>();
        int defaultLanguageKeys = Files.getNumberOfText("en");
        File file = Gdx.files.internal("languages/").file();
        if (file.exists() && file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                String fileName = subFile.getName();
                String t[] = fileName.split("\\.");
                if (LANGUAGES_NAMES.containsKey(t[0])) { // if is a referenced language.
                    int languageKeys = Files.getNumberOfText(t[0]);
                    float percent = (float) languageKeys / (float) defaultLanguageKeys * 100;
                    LANGUAGES_PERCENTAGES.put(t[0], (int) (percent));
                }
            }
        } else {
            LANGUAGES_PERCENTAGES.put("en", 100);
        }
    }

    // private static void saveLanguagePercentages() {
    // if (LANGUAGES_PERCENTAGES == null) {
    // calculateLanguagesPercentages();
    // }
    // Files.saveMapToCSVFile("languages/languagesPercents.csv", LANGUAGES_PERCENTAGES, true);
    // }

    /**
     * {@summary Return color depending of percent.}
     * 
     * @param percent score percent [0;100]
     * @return color
     */
    public static Color getColorFromPercent(int percent) {
        if (percent == 100) {
            return Color.GREEN;
        } else {
            return new Color(1f, percent / 100f, 0f, 1f);
        }
    }

}

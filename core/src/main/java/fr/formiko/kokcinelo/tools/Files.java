package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import fr.formiko.usual.Os;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * {@summary Tool class to manage files.}
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
public class Files {
    private static Yaml yaml;
    /**
     * @return a Yaml object initialized by lazy.
     */
    private static Yaml getYaml() {
        if (yaml == null) {
            yaml = new Yaml();
        }
        return yaml;
    }
    /**
     * @return the data path depending of the OS.
     */
    public static String getDataPath() {
        if (Os.getOs().isWindows()) {
            return System.getenv("APPDATA") + "/.kokcinelo/data/";
        } else {
            return System.getProperty("user.home") + "/.kokcinelo/data/";
        }
    }

    /**
     * Get the translated text from a json file. The file must be in the folder languages and have the name of the language code.
     * 
     * @param languageCode language 2 or 3 char code of the file to load.
     * @return the translated text.
     */
    public static HashMap<String, String> getText(String languageCode) {
        try {
            // InputStream is = new Files().getClass().getClassLoader().getResourceAsStream("languages/" + languageCode + ".json");
            // App.log(1, "", "InputStream : " + is);
            FileHandle file = Gdx.files.internal("languages/" + languageCode + ".yml");
            InputStream is = file.read();
            // return new ObjectMapper().readValue(file.file(), HashMap.class);
            HashMap<String, String> map = getYaml().load(is);
            App.log(0, "", "translated text map: " + map);
            return map;
        } catch (Exception e) {
            App.log(2, "", "fail to load translated map " + e);
            return new HashMap<String, String>();
        }
    }
    /**
     * @return The list of not null keys.
     */
    public static int getNumberOfText(String languageCode) {
        Map<String, String> map = getText(languageCode);
        int cpt = 0;
        for (Object key : map.keySet()) {
            if (map.get(key) != null) {
                cpt++;
            }
        }
        return cpt;
    }

    /**
     * Save a map in a file.
     * 
     * @param fileName name of the file to save data in
     * @param content  data to save in file
     */
    public static void saveInFile(String fileName, Map<String, String> content) {
        getYaml().dump(content, Gdx.files.absolute(getDataPath() + fileName).writer(false));
    }

    /**
     * Load a map from a file.
     * 
     * @param fileName name of the file to load data from
     * @return the data loaded from file
     */
    public static HashMap<String, String> loadFromFile(String fileName, boolean internal) {
        FileHandle file;
        if (internal) {
            file = Gdx.files.internal(fileName);
        } else {
            file = Gdx.files.absolute(Files.getDataPath() + fileName);
        }
        return getYaml().load(file.read());
    }
    /**
     * Load a map from a file.
     * 
     * @param fileName name of the file to load data from
     * @return the data loaded from file
     */
    public static HashMap<String, String> loadMapFromCSVFile(String fileName, boolean internal) {
        HashMap<String, String> map = new HashMap<String, String>();
        FileHandle file;
        if (internal) {
            file = Gdx.files.internal(fileName);
        } else {
            file = Gdx.files.absolute(Files.getDataPath() + fileName);
        }
        if (file.exists()) {
            for (String line : file.readString().strip().split("\\r?\\n")) {
                if (!line.isEmpty()) {
                    String t[] = line.split(",");
                    if (t.length == 2) {
                        map.put(t[0], t[1]);
                    }
                }
            }
        }
        return map;
    }
    /**
     * Save a map from a file.
     * 
     * @param fileName name of the file to load data from
     * @param map      the map to save
     */
    public static void saveMapToCSVFile(String fileName, Map<String, ?> map) {
        String s = "";
        for (String key : map.keySet()) {
            s += key + "," + map.get(key) + "\n";
        }
        FileHandle file = Gdx.files.absolute(Files.getDataPath() + fileName);
        file.writeString(s, false);
    }
}

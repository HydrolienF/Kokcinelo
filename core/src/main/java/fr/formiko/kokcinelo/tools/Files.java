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
            App.log(1, "", "translated text map: " + map);
            return map;
        } catch (Exception e) {
            App.log(2, "", "fail to load translated map " + e);
            return new HashMap<String, String>();
        }
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
    public static HashMap<String, String> loadFromFile(String fileName) {
        return getYaml().load(Gdx.files.absolute(getDataPath() + fileName).read());
    }
}

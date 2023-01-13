package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import fr.formiko.usual.Os;
import java.io.InputStream;
import java.util.HashMap;
import org.yaml.snakeyaml.Yaml;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Files {
    public static String getDataPath() {
        if (Os.getOs().isWindows()) {
            return System.getenv("APPDATA") + "/.kokcinelo/data/";
        } else {
            return System.getProperty("user.home") + "/.kokcinelo/data/";
        }
    }

    public static HashMap<String, String> getText(String languageCode) {
        try {
            // InputStream is = new Files().getClass().getClassLoader().getResourceAsStream("languages/" + languageCode + ".json");
            // App.log(1, "", "InputStream : " + is);
            FileHandle file = Gdx.files.internal("languages/" + languageCode + ".yml");
            InputStream is = file.read();
            // return new ObjectMapper().readValue(file.file(), HashMap.class);
            Yaml yaml = new Yaml();
            HashMap<String, String> map = yaml.load(is);
            App.log(1, "", "translated text map: " + map);
            return map;
        } catch (Exception e) {
            App.log(2, "", "fail to load translated map " + e);
            return new HashMap<String, String>();
        }
    }
}

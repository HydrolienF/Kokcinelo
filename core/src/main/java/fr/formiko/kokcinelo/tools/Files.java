package fr.formiko.kokcinelo.tools;

import fr.formiko.usual.Os;

public class Files {
    public static String getDataPath() {
        if (Os.getOs().isWindows()) {
            return System.getenv("APPDATA") + "/.kokcinelo/data/";
        } else {
            return System.getProperty("user.home") + "/.kokcinelo/data/";
        }
    }
}

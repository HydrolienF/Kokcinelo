package fr.formiko.kokcinelo.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * {@summary Tool class to manage options.}
 * It extends HashMap&lt;String, String&gt; to add some methods to get and put int, float and boolean.
 */
public class OptionsMap extends HashMap<String, String> {
    public OptionsMap(Map<String, String> m) {
        this();
        this.putAll(m);
    }

    public OptionsMap() { super(); }


    public int getInt(String key) { return Integer.parseInt(this.get(key)); }
    public void putInt(String key, int value) { this.put(key, Integer.toString(value)); }
    public float getFloat(String key) { return Float.parseFloat(this.get(key)); }
    public void putFloat(String key, float value) { this.put(key, Float.toString(value)); }
    public boolean getBoolean(String key) { return Boolean.parseBoolean(this.get(key)); }
    public void putBoolean(String key, boolean value) { this.put(key, Boolean.toString(value)); }
}

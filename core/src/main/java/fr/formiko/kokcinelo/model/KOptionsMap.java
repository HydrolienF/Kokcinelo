package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.tools.OptionsMap;
import java.util.Map;

public class KOptionsMap extends OptionsMap {
    public KOptionsMap(Map<String, String> m) { super(m); }
    public KOptionsMap() { super(); }

    // personalized getter & setter
    public float getMusicVolume() { return this.getFloat("musicVolume"); }
    public void setMusicVolume(float value) { this.putFloat("musicVolume", value); }
    public float getSoundVolume() { return this.getFloat("soundVolume"); }
    public void setSoundVolume(float value) { this.putFloat("soundVolume", value); }
}

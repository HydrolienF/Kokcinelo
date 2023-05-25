package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.tools.OptionsMap;
import fr.formiko.usual.g;
import java.util.List;
import java.util.Map;

public class KOptionsMap extends OptionsMap {
    public KOptionsMap(Map<String, String> m) { super(m); }
    public KOptionsMap() { super(); }

    // personalized getter & setter
    public float getMusicVolume() { return this.getFloat("musicVolume"); }
    public void setMusicVolume(float value) { this.putFloat("musicVolume", value); }
    public float getSoundVolume() { return this.getFloat("soundVolume"); }
    public void setSoundVolume(float value) { this.putFloat("soundVolume", value); }
    public int getDisplayMode() { return this.getInt("displayMode"); }
    public void setDisplayMode(int value) { this.putInt("displayMode", value); }
    public List<String> getListOfDisplayMode() { return List.of(g.get("FullScreen"), g.get("WindowedFullScreen"), g.get("Windowed")); }
}

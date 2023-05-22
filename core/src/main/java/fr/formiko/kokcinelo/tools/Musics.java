package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import java.util.List;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Null;

/**
 * {@summary Tools class to manage music.}
 * It aways play only one music at a time.
 * It have a null safety for every function.
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 0.2
 */
public class Musics {
    private static @Null Music music;
    // @formatter:off
    private static Map<String, List<Object>> levelsMusic = Map.ofEntries(
        Map.entry("1K", List.of("Waltz of the Night", 0.8f)),
        Map.entry("2K", List.of("Sailors Of The Dvipa", 0.5f)),
        Map.entry("3K", List.of("Madness", 1f)),
        Map.entry("2F", List.of("Hako 1", 1f)),
        Map.entry("3F", List.of("Hako 2", 0.8f))
    );
    // @formatter:on

    private Musics() {}

    public static float getMusicsVolume() { return App.getOptionsMap().getFloat("musicVolume"); }
    /**
     * Set the current music.
     * 
     * @param fileName name of the music file to load from musics/. It must be a mp3 file.
     */
    public static void setMusic(String fileName) {
        App.log(1, "Set music to " + fileName);
        stop();
        dispose();
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/" + fileName + ".mp3"));
        music.setVolume(getMusicsVolume());
        App.log(1, "Current music : " + fileName);
    }
    /**
     * Play the current music.
     */
    public static void play() {
        stop();
        if (music != null) {
            App.log(1, "Play current music");
            music.play();
        }
    }
    /**
     * Resume the current music.
     */
    public static void resume() {
        if (music != null) {
            App.log(1, "Play current music");
            music.play();
        }
    }
    /**
     * Set the current music and play it.
     * 
     * @param fileName name of the music file to load from musics/. It must be a mp3 file.
     */
    public static void play(String fileName) {
        setMusic(fileName);
        play();
    }
    /**
     * Pause the current music.
     */
    public static void pause() {
        if (music != null) {
            music.pause();
        }
    }
    /**
     * Stop the current music.
     */
    public static void stop() {
        if (music != null) {
            music.stop();
        }
    }
    /**
     * Dispose the current music.
     */
    public static void dispose() {
        if (music != null) {
            music.dispose();
        }
    }
    /**
     * Set the volume of the current music.
     * 
     * @param volume volume of the music. It must be between 0 and 1.
     */
    public static void setVolume(float volume) {
        if (music != null) {
            music.setVolume(volume * getMusicsVolume());
        }
    }
    /**
     * Set if the current music is looping.
     * 
     * @param isLooping true if the music is looping.
     */
    public static void setLooping(boolean isLooping) {
        if (music != null) {
            music.setLooping(isLooping);
        }
    }

    /**
     * Play the menu music.
     */
    public static void playMenuMusic() {
        play("Sun Is Not Falling cut");
        setLooping(true);
    }

    /**
     * Set a level music.
     * 
     * @param levelId id of the level
     */
    public static void setLevelMusic(String levelId) {
        App.log(1, "Set music for level " + levelId);
        if (levelsMusic.containsKey(levelId)) {
            setMusic((String) levelsMusic.get(levelId).get(0) + " 1min");
            setVolume((float) levelsMusic.get(levelId).get(1));
        } else {
            setMusic("Waltz of the Night 1min");
            setVolume(0.8f);
        }
    }
}

package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * {@summary Tools class to manage music.}
 * It aways play only one music at a time.
 * It have a null safety for every function.
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
public class Musics {
    private static Music music;
    private static float musicsVolume = 1f;

    public static float getMusicsVolume() { return musicsVolume; }
    public static void setMusicsVolume(float musicsVolume) { Musics.musicsVolume = musicsVolume; }
    // public static Music getMusic() { return music; }
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
        music.setVolume(musicsVolume);
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
            music.setVolume(volume * musicsVolume);
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
    // public static void playGameMusic() { play("Sun Is Not Falling"); }

    /**
     * Set a level music.
     * 
     * @param levelId id of the level
     */
    public static void setLevelMusic(String levelId) {
        App.log(1, "Set music for level " + levelId);
        switch (levelId) {
        case "1K":
            setMusic("Waltz of the Night 1min");
            setVolume(0.8f);
            break;
        case "2K":
            setMusic("Sailors Of The Dvipa 1min");
            setVolume(0.5f);
            break;
        case "3K":
            setMusic("Madness 1min");
            break;
        case "4K":
            setMusic("");
            break;
        case "5K":
            setMusic("");
            break;
        case "2F":
            setMusic("Hako 1 1min");
            setVolume(0.8f);
            break;
        case "3F":
            setMusic("Hako 2 1min");
            setVolume(0.8f);
            break;
        case "4F":
            setMusic("");
            break;
        case "5F":
            setMusic("");
            break;
        case "4A":
            setMusic("");
            break;
        case "5A":
            setMusic("");
            break;
        default:
            setMusic("Waltz of the Night 1min");
            break;
        }
    }
}

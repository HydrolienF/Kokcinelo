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

    // public static Music getMusic() { return music; }
    /**
     * Set the current music.
     * 
     * @param fileName name of the music file to load from musics/. It must be a mp3 file.
     */
    public static void setMusic(String fileName) {
        stop();
        dispose();
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/" + fileName + ".mp3"));
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
            music.setVolume(volume);
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
}

package fr.formiko.kokcinelo.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Musics {
    private static Music music;

    // public static Music getMusic() { return music; }
    public static void setMusic(String fileName) {
        stop();
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/" + fileName + ".mp3"));
    }

    public static void play() {
        stop();
        if (music != null) {
            music.play();
        }
    }
    public static void play(String fileName) {
        setMusic(fileName);
        play();
    }
    public static void pause() {
        if (music != null) {
            music.pause();
        }
    }
    public static void stop() {
        if (music != null) {
            music.stop();
        }
    }
    public static void dispose() {
        if (music != null) {
            music.dispose();
        }
    }
    public static void setVolume(float volume) {
        if (music != null) {
            music.setVolume(volume);
        }
    }

    public static void playMenuMusic() { play("Sun Is Not Falling cut"); }
    // public static void playGameMusic() { play("Sun Is Not Falling"); }
}

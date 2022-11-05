package fr.formiko.kokcinelo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * {@summary Main class that represent the App &#38; call other Screens.}
 * It call MainMenuScreen
 * 
 * @see com.badlogic.gdx.Game
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class App extends Game {
    public SpriteBatch batch;
    private Sound eatingSound;
    private Music mainMusic;

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Main function call when App is created}
     * It call MainMenuScreen
     * 
     * @see com.badlogic.gdx.ApplicationListener#create()
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        // TODO add MainMenuScreen that will be call 1st & call GameMenu after
        startNewGame();
    }
    /**
     * {@summary Start a new Game on this app in GUI.}
     */
    public void startNewGame() {
        Controller c = new Controller(this);
        Controller.setController(c);
        c.createNewGame();
    }

    /***
     * {@summary call render function of sub screens}
     * 
     * @see com.badlogic.gdx.Game#render()
     */
    @Override
    public void render() { super.render(); }

    /**
     * {@summary call dispose function of item that need to be dispose so that App close itself.}
     * 
     * @see com.badlogic.gdx.Game#dispose()
     */
    @Override
    public void dispose() {
        // TODO be able to dispose even for html version
        System.exit(0);
    }

    // music --------------------------------------------------------------------------------------
    /**
     * {@summary Play the game music.}
     */
    public void playGameMusic() {
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/Waltz of the Night 1min.mp3"));
        mainMusic.play();
    }
    /**
     * {@summary Play the eating sound.}
     * Sound can be play many times &#38; at same time.
     */
    public void playEatingSound() {
        if (eatingSound == null) {
            eatingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crock.mp3"));
        }
        eatingSound.play();
    }
    /**
     * {@summary Play the end game sound.}
     * Sound can be play many times &#38; at same time.
     * 
     * @param haveWin the sound is different if game is win or lost
     */
    public void playEndGameSound(boolean haveWin) {
        String fileName;
        if (haveWin) {
            fileName = "win";
        } else {
            fileName = "lose";
        }
        Sound s = Gdx.audio.newSound(Gdx.files.internal("sounds/" + fileName + ".mp3"));
        mainMusic.stop();
        s.play();
    }
}

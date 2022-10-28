package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.view.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * {@summary Main class that represent the App &#38; call other Screens.}
 * It call MainMenuScreen
 * 
 * @see com.badlogic.gdx.Game
 */
public class App extends Game {
    public SpriteBatch batch;
    Screen currentScreen;
    private Sound eatingSound;
    private Music mainMusic;

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
    public void startNewGame() {
        currentScreen = new GameScreen(this);
        this.setScreen(currentScreen);
    }

    /***
     * {@summary call render function of sub screens}
     * 
     * @see com.badlogic.gdx.Game#render()
     */
    @Override
    public void render() { super.render(); }

    /***
     * {@summary call dispose function of item that need to be dispose so that App close itself.}
     * 
     * @see com.badlogic.gdx.Game#dispose()
     */
    @Override
    public void dispose() { System.exit(0); }

    // music --------------------------------------------------------------------------------------
    public void playGameMusic() {
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/Waltz of the Night 1min.mp3"));
        mainMusic.play();
    }
    public void playEatingSound() {
        if (eatingSound == null) {
            eatingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crock.mp3"));
        }
        eatingSound.play();
    }
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

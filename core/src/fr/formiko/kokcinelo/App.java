package fr.formiko.kokcinelo;

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
    SpriteBatch batch;
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
        // TODO add MainMenuScreen that will be call 1st & call GameMenu after
        batch = new SpriteBatch();
        currentScreen = new GameScreen(this);
        this.setScreen(currentScreen);
        playMusic();
    }

    /**
     * {@summary call render function of sub screens}
     * 
     * @see com.badlogic.gdx.Game#render()
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * {@summary call dispose function of item that need to be dispose so that App
     * close itself.}
     * 
     * @see com.badlogic.gdx.Game#dispose()
     */
    @Override
    public void dispose() {
        // batch.dispose();
        // try {
        // batch.end();
        // batch.dispose();
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        System.exit(0);
    }

    private void playMusic(){
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/Waltz of the Night.mp3"));
        mainMusic.setLooping(true);
        mainMusic.play();
    }
    public void playEatingSound(){
        if(eatingSound==null){
            eatingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crock.mp3"));
        }
        eatingSound.play();
    }
}

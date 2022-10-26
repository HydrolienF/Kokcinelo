package fr.formiko.kokcinelo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
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
}

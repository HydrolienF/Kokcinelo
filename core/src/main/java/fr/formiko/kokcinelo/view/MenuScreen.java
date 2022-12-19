package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@summary The main menu screen.}
 * 
 * @see com.badlogic.gdx.Screen
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
public class MenuScreen implements Screen {
    private Stage stage;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     */
    public MenuScreen() {
        addInputCore();
        stage = new Stage();
        App.log(0, "constructor", "new MenuScreen: " + toString());

    }
    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Render the screen.}
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(App.BLUE_BACKGROUND);
        stage.act();
    }

    /**
     * {@summary Dispose all variable that need to be dispose to save memory.}
     * 
     * @see com.badlogic.gdx.Game#dispose()
     */
    @Override
    public void dispose() {
        App.log(0, "destructor", "dispose MenuScreen: " + toString());
        stage.dispose();
    }

    // TODO Auto-generated method stub
    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    /**
     * {@summary Handle user input.}<br>
     */
    private void addInputCore() {
        InputProcessor inputProcessor = (InputProcessor) new InputProcessor() {

            @Override
            public boolean keyDown(int keycode) { return false; }
            /**
             * {@summary React to key pressed once.}
             * 
             * @param keycode the key pressed
             */
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
                    Controller.getController().endMenuScreen();
                }
                return true;
            }

            @Override
            public boolean keyTyped(char character) { return false; }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

            @Override
            public boolean mouseMoved(int screenX, int screenY) { return false; }

            @Override
            public boolean scrolled(float amountX, float amountY) { return false; }

        };
        Gdx.input.setInputProcessor(inputProcessor);
    }

}

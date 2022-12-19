package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {

    public MenuScreen() {
        addInputCore();
        App.log(0, "constructor", "new MenuScreen: " + toString());
    }

    @Override
    public void show() { // TODO Auto-generated method stub
    }

    @Override
    public void render(float delta) { ScreenUtils.clear(App.BLUE_BACKGROUND); }


    @Override
    public void resize(int width, int height) { // TODO Auto-generated method stub
    }

    @Override
    public void pause() { // TODO Auto-generated method stub
    }

    @Override
    public void resume() { // TODO Auto-generated method stub
    }

    @Override
    public void hide() { // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        App.log(0, "destructor", "dispose MenuScreen: " + toString());
        // TODO
    }

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

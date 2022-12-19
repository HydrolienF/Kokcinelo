package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.view.GameScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * {@summary React to user inputs.}
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.1
 */
public class InputCore implements InputProcessor {

    private GameScreen screen;

    // CONSTRUCTORS --------------------------------------------------------------
    /***
     * {@summary Main constructor.}
     * 
     * @param screen screen to interact with
     */
    public InputCore(GameScreen screen) { this.screen = screen; }

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Zoom in or out for the camera.}
     * 
     * @see com.badlogic.gdx.InputProcessor#scrolled(float, float)
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // System.out.println("mouse scrolled of "+amountX+" "+amountY);
        Controller.getController().addZoom(amountY);
        return true;
    }

    /**
     * {@summary React to key pressed once.}
     * 
     * @param keycode the key pressed
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.P) {
            Controller.getController().pauseResume();
        }
        if (keycode == Input.Keys.S) {
            Controller.getController().gameOver();
        }
        if ((keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) && screen.isStop()) {
            Controller.getController().endGameScreen();
        }

        return true;
    }
    // Not usefull yet.
    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyTyped(char character) { return false; }
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) { return false; }
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) { return false; }
    @Override
    public boolean touchDragged(int x, int y, int pointer) { return false; }
    @Override
    public boolean mouseMoved(int x, int y) { return false; }
}

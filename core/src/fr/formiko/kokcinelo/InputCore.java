package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.view.GameScreen;
import com.badlogic.gdx.InputProcessor;

/**
 * {@summary React to user inputs.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class InputCore implements InputProcessor {

    GameScreen screen;

    // CONSTRUCTORS --------------------------------------------------------------
    public InputCore(GameScreen screen) { this.screen = screen; }
    public InputCore() {}

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Zoom in or out for the camera.}
     * 
     * @see com.badlogic.gdx.InputProcessor#scrolled(float, float)
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // System.out.println("mouse scrolled of "+amountX+" "+amountY);
        screen.getController().addZoom(amountY);
        return true;
    }


    // Not usefull yet.
    @Override
    public boolean keyDown(int keycode) { return false; }
    @Override
    public boolean keyUp(int keycode) { return false; }
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

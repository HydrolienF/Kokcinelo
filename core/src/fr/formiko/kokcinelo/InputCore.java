package fr.formiko.kokcinelo;

import com.badlogic.gdx.InputProcessor;

/**
 * {@summary React to user inputs.}
 */
public class InputCore implements InputProcessor {

    GameScreen screen;

    public InputCore(GameScreen screen) {
        this.screen = screen;
    }

    public InputCore() {
    }

    /**
     * {@summary Zoom in or out for the camera.}
     * 
     * @see com.badlogic.gdx.InputProcessor#scrolled(float, float)
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // System.out.println("mouse scrolled of "+amountX+" "+amountY);
        screen.camera.zoom += amountY * 0.05f;
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }

}

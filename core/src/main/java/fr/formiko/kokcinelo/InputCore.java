package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Creature;
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
        if (keycode == Input.Keys.P || keycode == Input.Keys.ESCAPE) {
            Controller.getController().pauseResume();
        }
        if (keycode == Input.Keys.S) {
            Controller.getController().gameOver();
        }
        // space key have been remove because player migth want to use it to shoot & end game screen by mistake.
        if (keycode == Input.Keys.ENTER && screen.isStop()) {
            // Controller.getController().endGameScreen();
            Controller.getController().createNewMenuScreen();
        } else if (keycode == Input.Keys.SPACE && !screen.isPause()) {
            Creature c = Controller.getController().getPlayerCreature();
            if (c instanceof Ant) {
                Controller.getController().antShoot((Ant) c);
            }
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

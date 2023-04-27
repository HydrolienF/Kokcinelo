package fr.formiko.kokcinelo.model;

/**
 * {@summary Ladybug that display in side view.}
 * It is used by menu.
 * 
 * @author Hydrolien
 * @version 1.2
 * @since 1.2
 */
public class LadybugSideView extends Ladybug {
    public LadybugSideView() {
        super("ladybugSideView");
        setMovingSpeed(movingSpeed * 3);
    }
}

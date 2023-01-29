package fr.formiko.kokcinelo.model;

/**
 * {@summary Ants are creatures that figth ladybugs to protect aphids.}
 * This type of Ant use acid to kill ladybugs.
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class GreenAnt extends Ant {
    /**
     * {@summary Create new Ants.}
     */
    public GreenAnt() {
        super();
        hitPoints = 0;
        shootPoints = 25;
        shootFrequency = 2000;
    }
}

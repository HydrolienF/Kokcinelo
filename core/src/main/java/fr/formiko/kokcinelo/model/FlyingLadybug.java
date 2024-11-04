package fr.formiko.kokcinelo.model;

public class FlyingLadybug extends Ladybug {
    public FlyingLadybug(String textureName) {
        super(textureName);
        canFly = true;
    }
    public FlyingLadybug() { this("ladybug"); }
}

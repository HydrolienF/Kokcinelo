package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GameState;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Controller {
    private GameState gs;
    private static Random ran;
    private App app;
    private GameScreen gScreen;

    public Controller(App app, GameScreen gScreen) {
        this.app=app;
        this.gScreen=gScreen;
    }

    public void addZoom(float amountY){
        getCamera().zoom += amountY * 0.05f;
        // gScreen.replaceMaskBy(new Circle(getCamera().position.x,getCamera().position.y,300*getCamera().zoom));
    }

    // TODO move the player camera & Creature
    public void movePlayer(int playerId) {

    }

    public void createNewGame() {
        gs = GameState.builder()
                .setMapHeight(1000)
                .setMapWidth(2000)
                .build();
    }

    public void updateActorVisibility(int playerId) {
        gs.updateActorVisibility(playerId);
    }

    public Iterable<Creature> allCreatures() {
        return gs.allCreatures();
    }
    public Iterable<Actor> allActors(){
        return gs.allActors();
    }

    public void synchonisePlayerCreatureWithCamera(OrthographicCamera camera, int playerId) {
        Creature c = gs.getPlayerCreature(playerId);
        c.getActor().setX(camera.position.x - c.getActor().getWidth() / 2);
        c.getActor().setY(camera.position.y - c.getActor().getHeight() / 2);
        if(gs.getMapActorFg()!=null){
            gs.getMapActorFg().setX(c.getActor().getCenterX()-gs.getMapActorFg().getWidth()/2);
            gs.getMapActorFg().setY(c.getActor().getCenterY()-gs.getMapActorFg().getHeight()/2);
        }
    }

    public static Random getRandom() {
        if (ran == null) {
            ran = new Random();
        }
        return ran;
    }

    private OrthographicCamera getCamera(){
        return gScreen.camera;
    }
}

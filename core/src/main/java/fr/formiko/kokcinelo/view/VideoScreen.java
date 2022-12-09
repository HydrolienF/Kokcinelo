package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.MapItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class VideoScreen implements Screen {
    private final App game;
    private Stage stage;
    private static OrthographicCamera camera;
    private Viewport viewport;

    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game the App where the Screen is display
     */
    public VideoScreen(final App game) {
        this.game = game;

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(w * 0.5f, h * 0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        // Texture t = new Texture(Gdx.files.internal("images/" + "videoBackground" + ".png"));
        Actor actor = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                // batch.draw(new TextureRegion(t), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(),
                // getScaleY(), getRotation());
            }
        };
        actor.setSize((int) (w * 1.5), (int) (h * 1.5));
        stage.addActor(actor);
        // TODO form a groups off actor or load all images in one actor where we can control rotation of the wings.
        loadAnimateLadybug();

        App.log(0, "constructor", "new VideoScreen: " + toString());
    }

    public static OrthographicCamera getCamera() { return camera; }

    @Override
    public void show() { // TODO Auto-generated method stub
    }

    /**
     * {@summary Draw all thing that need to be draw during a game.}
     * 
     * @see com.badlogic.gdx.Screen#render(float)
     */
    @Override
    public void render(float delta) {
        handleInput();
        ScreenUtils.clear(0, 1, 0, 1);
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
    }

    private void handleInput() {
        // while debuging game close on escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            game.dispose();
    }

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
    public void dispose() { // TODO Auto-generated method stub
    }


    private void loadAnimateLadybug() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float duration = 0.06f;
        float rotationMin = 10;
        float rotationMax = 40;

        float bodyDuration = 1f;
        float elytraRotation = 3f;
        float headRotation = 0.3f;
        float mandibleRotation = 5f;
        float antennaRotation = 15f;
        float legRotation = -5f;

        MapItem mi = new Ladybug();
        MapItemActorAnimate ladybug = new MapItemActorAnimate("Creatures/ladybug flying side view body", mi);


        MemberActor wing = new MemberActor("Creatures/ladybug flying side view wing", mi);
        float lbw = ladybug.getWidth();
        float lbh = ladybug.getHeight();
        wing.addToOrigin(280, 0);
        wing.addAvailableAction(Actions.sequence(Actions.rotateTo(-rotationMax, duration * (rotationMax / (rotationMax + rotationMin))),
                Actions.forever(Actions.sequence(Actions.rotateTo(rotationMin, duration), Actions.rotateTo(-rotationMax, duration)))));
        ladybug.addMember(wing);


        MemberActor leg1 = new MemberActor("Creatures/ladybug flying side view leg1", mi);
        leg1.setOrigin(930, lbh - 937);
        leg1.addAvailableAction(Actions.sequence(Actions.rotateTo(-legRotation, bodyDuration / 2), Actions
                .forever(Actions.sequence(Actions.rotateTo(legRotation, bodyDuration), Actions.rotateTo(-legRotation, bodyDuration)))));
        ladybug.addMember(leg1);

        MemberActor leg2 = new MemberActor("Creatures/ladybug flying side view leg2", mi);
        leg2.setOrigin(1100, lbh - 920);
        leg2.addAvailableAction(Actions.sequence(Actions.rotateTo(-legRotation, bodyDuration / 2), Actions
                .forever(Actions.sequence(Actions.rotateTo(legRotation, bodyDuration), Actions.rotateTo(-legRotation, bodyDuration)))));
        ladybug.addMember(leg2);

        MemberActor leg3 = new MemberActor("Creatures/ladybug flying side view leg3", mi);
        leg3.setOrigin(1145, lbh - 895);
        leg3.addAvailableAction(Actions.sequence(Actions.rotateTo(-legRotation, bodyDuration / 2), Actions
                .forever(Actions.sequence(Actions.rotateTo(legRotation, bodyDuration), Actions.rotateTo(-legRotation, bodyDuration)))));
        ladybug.addMember(leg3);


        MemberActor body = new MemberActor("Creatures/ladybug flying side view body", mi);
        ladybug.addMember(body);


        MemberActor head = new MemberActor("Creatures/ladybug flying side view head", mi);
        head.addToOrigin(400, 60);
        head.addAvailableAction(Actions.sequence(Actions.rotateTo(-headRotation, bodyDuration / 2), Actions
                .forever(Actions.sequence(Actions.rotateTo(headRotation, bodyDuration), Actions.rotateTo(-headRotation, bodyDuration)))));
        ladybug.addMember(head);

        MemberActor mandible = new MemberActor("Creatures/ladybug flying side view mandible", mi);
        mandible.setOrigin(1397, lbh - 905);
        mandible.addAvailableAction(Actions.sequence(Actions.rotateTo(-mandibleRotation, bodyDuration / 2), Actions.forever(
                Actions.sequence(Actions.rotateTo(mandibleRotation, bodyDuration), Actions.rotateTo(-mandibleRotation, bodyDuration)))));
        head.addMember(mandible);

        MemberActor antenna = new MemberActor("Creatures/ladybug flying side view antenna", mi);
        antenna.setOrigin(1401, lbh - 872);
        antenna.addAvailableAction(Actions.sequence(Actions.rotateTo(-antennaRotation, bodyDuration / 2), Actions.forever(
                Actions.sequence(Actions.rotateTo(antennaRotation, bodyDuration), Actions.rotateTo(-antennaRotation, bodyDuration)))));
        head.addMember(antenna);


        MemberActor elytra = new MemberActor("Creatures/ladybug flying side view elytra", mi);
        elytra.addToOrigin(360, 30);
        elytra.addAvailableAction(Actions.sequence(Actions.rotateTo(-elytraRotation, bodyDuration / 2), Actions.forever(
                Actions.sequence(Actions.rotateTo(elytraRotation, bodyDuration), Actions.rotateTo(-elytraRotation, bodyDuration)))));
        ladybug.addMember(elytra);


        MemberActor wing2 = new MemberActor("Creatures/ladybug flying side view wing", mi);
        wing2.setOrigin(1076, lbh - 791);
        wing2.addAvailableAction(Actions.sequence(Actions.delay(duration / 15),
                Actions.rotateTo(-rotationMax, duration * (rotationMax / (rotationMax + rotationMin))),
                Actions.forever(Actions.sequence(Actions.rotateTo(rotationMin, duration), Actions.rotateTo(-rotationMax, duration)))));
        ladybug.addMember(wing2);


        ladybug.startAction(0);
        ladybug.setZoom(0.8f);
        stage.addActor(ladybug);
        // ladybug.setDebug(true, true);
        ladybug.setCenterX(w / 2);
        ladybug.setCenterY(h / 2);
    }
}

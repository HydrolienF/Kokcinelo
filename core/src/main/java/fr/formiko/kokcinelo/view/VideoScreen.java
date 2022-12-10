package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.MapItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
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
    private Music music;
    private float targetZoom;

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
                // batch.draw(new TextureRegion(t), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                // getScaleX(),
                // getScaleY(), getRotation());
            }
        };
        actor.setSize((int) (w * 1.5), (int) (h * 1.5));
        stage.addActor(actor);
        // TODO form a groups off actor or load all images in one actor where we can control rotation of the wings.
        MapItemActorAnimate ladybug = loadAnimateLadybug();
        ladybug.addAction(Actions.sequence(Actions.delay(1f), Actions.run(new Runnable() {
            public void run() { targetZoom = 20f; }
            // public void run() { camera. }
        }), Actions.sequence(Actions.delay(1f), Actions.run(new Runnable() {
            public void run() { ladybug.setSpeed(3); }
        }), Actions.parallel(Actions.moveBy(200, 0, 1f, Interpolation.swingOut), Actions.rotateTo(20, 1f)),
                Actions.parallel(Actions.moveBy(200, 0, 0.5f, Interpolation.swingOut), Actions.rotateTo(-60, 0.5f, Interpolation.swingOut)),
                Actions.delay(3f), Actions.run(new Runnable() {
                    public void run() { ladybug.setSpeed(2.4f); }
                }), Actions.rotateTo(0, 0.5f), Actions.delay(1f), Actions.run(new Runnable() {
                    public void run() { ladybug.setSpeed(1); }
                }), Actions.moveBy(-400, 0, 1f, Interpolation.swingOut), Actions.delay(3f), Actions.run(new Runnable() {
                    public void run() { dispose(); }
                }))));
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/Waltz of the Night.mp3"));
        // music.play();
        camera.zoom = 5f;
        targetZoom = 5f;

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
        ScreenUtils.clear(0, 203f / 255, 1, 1);
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
        if (camera.zoom < targetZoom) {
            camera.zoom = Math.min(camera.zoom * 1.03f, targetZoom);
        } else if (camera.zoom > targetZoom) {
            camera.zoom = Math.max(camera.zoom / 1.03f, targetZoom);
        }
    }

    private void handleInput() {
        // while debuging game close on escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            dispose();
        }
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
    public void dispose() {
        game.dispose();// @a
        Controller.getController().createNewGame();
        music.stop();
    }


    private MapItemActorAnimate loadAnimateLadybug() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float duration = 0.2f;
        float rotationMin = 10;
        float rotationMax = 40;
        float angleChangeBySpeed = 20f;

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
        wing.addAvailableAction(loopAction(ladybug, duration, rotationMin, rotationMax, wing, 0f, true));
        ladybug.addMember(wing);


        MemberActor leg;
        for (int j = 1; j > -1; j--) {
            for (int i = 0; i < 3; i++) {
                leg = new MemberActor("Creatures/ladybug flying side view leg" + (i + 1), mi);
                if (i == 0) {
                    leg.setOrigin(930, lbh - 937);
                } else if (i == 1) {
                    leg.setOrigin(1100, lbh - 920);
                } else {
                    leg.setOrigin(1145, lbh - 895);
                }
                leg.addAction(Actions.moveBy(j * -6, 0, 0f));
                leg.addAction(Actions.scaleBy(-j / 10f, -j / 10f, 1f));
                leg.addAvailableAction(loopAction(ladybug, bodyDuration, legRotation, legRotation, leg, (float) j / 2, false));
                leg.setAngleChangeBySpeed(angleChangeBySpeed);
                ladybug.addMember(leg);
            }
        }


        MemberActor body = new MemberActor("Creatures/ladybug flying side view body", mi);
        ladybug.addMember(body);


        MemberActor head = new MemberActor("Creatures/ladybug flying side view head", mi);
        head.addToOrigin(400, 60);
        head.addAvailableAction(loopAction(ladybug, bodyDuration, headRotation, headRotation, head, 0f, false));
        ladybug.addMember(head);

        MemberActor mandible = new MemberActor("Creatures/ladybug flying side view mandible", mi);
        mandible.setOrigin(1397, lbh - 905);
        mandible.addAvailableAction(loopAction(ladybug, bodyDuration, mandibleRotation, mandibleRotation, mandible, 0f, false));
        mandible.setAngleChangeBySpeed(10);
        head.addMember(mandible);

        for (int j = 1; j > -1; j--) {
            MemberActor antenna = new MemberActor("Creatures/ladybug flying side view antenna", mi);
            antenna.setOrigin(1401, lbh - 872);
            antenna.addAction(Actions.moveBy(j * -5, 0, 0f));
            antenna.addAction(Actions.scaleBy(-j / 50f, -j / 50f, 1f));
            antenna.addAvailableAction(loopAction(ladybug, bodyDuration, antennaRotation, antennaRotation, antenna, (float) j / 10, false));
            antenna.setAngleChangeBySpeed(-50);
            head.addMember(antenna);
        }


        MemberActor elytra = new MemberActor("Creatures/ladybug flying side view elytra", mi);
        elytra.addToOrigin(360, 30);
        elytra.addAvailableAction(Actions.sequence(Actions.rotateTo(-elytraRotation, bodyDuration / 2), Actions.forever(
                Actions.sequence(Actions.rotateTo(elytraRotation, bodyDuration), Actions.rotateTo(-elytraRotation, bodyDuration)))));
        ladybug.addMember(elytra);


        MemberActor wing2 = new MemberActor("Creatures/ladybug flying side view wing", mi);
        wing2.setOrigin(1076, lbh - 791);
        wing2.addAvailableAction(loopAction(ladybug, duration, rotationMin, rotationMax, wing2, duration / 5, true));
        ladybug.addMember(wing2);


        ladybug.startAction(0);
        ladybug.setZoom(0.8f);
        stage.addActor(ladybug);
        // ladybug.setDebug(true, true);
        ladybug.setCenterX(w / 4);
        ladybug.setCenterY(h / 2);

        return ladybug;
    }

    private Action loopAction(MapItemActorAnimate ladybug, float duration, float rotationMin, float rotationMax, MemberActor ma,
            float delay, boolean rotationSpeededBySpeed) {
        return Actions.sequence(Actions.delay(delay),
                Actions.rotateTo(-rotationMax, duration * (rotationMax / (rotationMax + rotationMin))), Actions.run(new Runnable() {
                    public void run() { ma
                            .addAction(loopedAction(ladybug, duration, rotationMin, rotationMax, ma, rotationSpeededBySpeed)); }
                }));
    }
    private Action loopedAction(MapItemActorAnimate ladybug, float duration, float rotationMin, float rotationMax, MemberActor ma,
            boolean rotationSpeededBySpeed) {
        float durationReal = duration;
        if (rotationSpeededBySpeed) {
            durationReal /= ladybug.getSpeed();
        }
        return Actions.sequence(Actions.rotateBy(rotationMin + rotationMax, durationReal),
                Actions.rotateBy(-(rotationMin + rotationMax), durationReal), Actions.run(new Runnable() {
                    public void run() { ma
                            .addAction(loopedAction(ladybug, duration, rotationMin, rotationMax, ma, rotationSpeededBySpeed)); }
                }));
    }
}

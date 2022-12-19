package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.MapItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
    private Sound flyingSound;
    private Sound crockSound;
    private Sound tingSound;
    private float targetZoom;
    private MapItemActorAnimate ladybug;
    private boolean drawRedCircle;
    private Aphid aphid;
    private MemberActor head;
    private MemberActor mandible;

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
        loadBackground();
        aphid = loadAphid();
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/Waltz of the Night.mp3"));
        flyingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/flying.mp3"));
        crockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crock.mp3"));
        tingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/ting.mp3"));

        music.setVolume(0.4f);
        music.play();
        long soundId = flyingSound.play(0.2f);
        flyingSound.setLooping(soundId, true);

        float lastDelay = 3;

        // Form a groups off actor or load all images in one actor where we can control rotation of the wings.
        ladybug = loadAnimateLadybug();
        ladybug.addAction(Actions.sequence(Actions.delay(0.5f), Actions.run(new Runnable() {
            public void run() { targetZoom = 4f; }
        }), Actions.sequence(Actions.delay(2f), Actions.run(new Runnable() {
            public void run() {
                tingSound.play();
                drawRedCircle = true;
            }
        }), Actions.delay(2f), Actions.run(new Runnable() {
            public void run() {
                targetZoom = 0.4f;
                drawRedCircle = false;
            }
        }), Actions.delay(0.5f), Actions.run(new Runnable() {
            public void run() { ladybug.setSpeed(3); }
        }), Actions.parallel(Actions.moveBy(100, 0, 0.5f, Interpolation.swingOut), Actions.rotateTo(20, 0.5f)),
                Actions.parallel(Actions.moveBy(1600, -1600, 1.5f), Actions.run(new Runnable() {
                    public void run() {
                        ladybug.setSpeed(2.4f);
                        flyingSound.setVolume(soundId, 1f);
                    }
                }), Actions.parallel(Actions.moveBy(100, 0, 0.5f, Interpolation.swingOut),
                        Actions.rotateTo(-60, 0.2f, Interpolation.swingOut)),
                        Actions.sequence(Actions.delay(1.1f), Actions.run(new Runnable() {
                            public void run() {
                                ladybug.setSpeed(1);
                                flyingSound.setVolume(soundId, 0.2f);
                            }
                        }), Actions.rotateTo(0, 0.5f))),
                Actions.parallel(Actions.moveBy(30, 0, 0.2f), Actions.run(new Runnable() {
                    public void run() {
                        crockSound.play();
                        head.addAction(Actions.rotateBy(20f, 0.1f, Interpolation.smooth));
                        mandible.addAction(Actions.rotateBy(60f, 0.1f));
                    }
                })), Actions.run(new Runnable() {
                    public void run() {
                        aphid.getActor().setVisible(false);
                        head.addAction(Actions.rotateBy(-20f, 0.1f, Interpolation.smooth));
                        mandible.addAction(Actions.rotateBy(-60f, 0.1f));
                    }
                }), Actions.delay(lastDelay), Actions.run(new Runnable() {
                    public void run() { dispose(); }
                }))));

        targetZoom = 0.5f;
        camera.zoom = targetZoom;

        addInputCore();

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
        ScreenUtils.clear(App.BLUE_BACKGROUND);
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
        if (camera.zoom < targetZoom) {
            camera.zoom = Math.min(camera.zoom * 1.08f, targetZoom);
            // camera.position = new Vector3(ladybug.getCenterX(), ladybug.getCenterY(), 1);
        } else if (camera.zoom > targetZoom) {
            camera.zoom = Math.max(camera.zoom / 1.08f, targetZoom);
        }
        camera.position.x = ladybug.getCenterX();
        camera.position.y = ladybug.getCenterY();
        // if (ladybug.getSpeed() > 1) {
        // ladybug.getMapItem().getActor().moveFront(ladybug.getSpeed() - 1);
        // }
        if (drawRedCircle) {
            Gdx.gl.glLineWidth(40);
            Creature c = (Creature) aphid;
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(getCamera().combined);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(new Color(1f, 0f, 0f, 1f));
            shapeRenderer.circle(aphid.getCenterX(), aphid.getCenterY(), (float) 300, 200);
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
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
        App.log(0, "destructor", "dispose VideoScreen: " + toString());
        flyingSound.dispose();
        crockSound.dispose();
        tingSound.dispose();
        music.dispose();
        stage.dispose();
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


        head = new MemberActor("Creatures/ladybug flying side view head", mi);
        head.addToOrigin(400, 60);
        head.addAvailableAction(loopAction(ladybug, bodyDuration, headRotation, headRotation, head, 0f, false));
        ladybug.addMember(head);

        mandible = new MemberActor("Creatures/ladybug flying side view mandible", mi);
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
        ladybug.setZoom(0.2f);
        stage.addActor(ladybug);
        // ladybug.setDebug(true, true);
        ladybug.setCenterX(w / 2);
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

    private void loadBackground() {
        MapActor ma = new MapActor(10000, 1000, new com.badlogic.gdx.graphics.Color(8 / 255f, 194 / 255f, 0 / 255f, 1f), true, 200, 80);
        ma.setPosition(-ma.getWidth() / 2, -1700);
        stage.addActor(ma);
        // TODO also add a cloud saying "KOKCINELO"
    }

    private Aphid loadAphid() {
        Aphid c = new Aphid();
        c.getActor().setZoom(0.1f);
        c.getActor().setCenterX(2900);
        c.getActor().setCenterY(-1100);
        stage.addActor(c.getActor());
        return c;
    }

    /**
     * {@summary Handle user input.}<br>
     */
    private void addInputCore() {
        InputProcessor inputProcessor = (InputProcessor) new InputProcessor() {

            @Override
            public boolean keyDown(int keycode) { return false; }
            /**
             * {@summary React to key pressed once.}
             * 
             * @param keycode the key pressed
             */
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
                    Controller.getController().endVideoScreen();
                }
                return true;
            }

            @Override
            public boolean keyTyped(char character) { return false; }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Controller.getController().endVideoScreen();
                return true;
            }

            /**
             * {@summary React to a mouse clic.}
             * 
             * @param screenX x screen location of the mouse
             * @param screenY y screen location of the mouse
             * @param pointer pointer id
             * @param button  mouse button id
             */
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

            @Override
            public boolean mouseMoved(int screenX, int screenY) { return false; }

            @Override
            public boolean scrolled(float amountX, float amountY) { return false; }

        };
        Gdx.input.setInputProcessor(inputProcessor);
    }
}

package fr.formiko.kokcinelo;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {*@summary The action game screen.}
 * 
 * @see com.badlogic.gdx.Screen
 */
public class GameScreen implements Screen {
    private final App game;
    private Viewport viewport;
    private Stage stage;

    static OrthographicCamera camera;
    private float rotationSpeed;
    private float maxZoom;
    private Controller controller;
    private int playerId;
    private Set<Circle> areaVisible;
    private SpriteBatch spriteBatch;
    private Texture masked;

    /**
     * {*@summary The action game screen constructor that load images &#39; set
     * Creatures locations.}
     * 
     * @param game the App where the Screen is display
     */
    public GameScreen(final App game) {
        // TODO move to GameState.java the state of current game
        this.game = game;
        controller = new Controller();
        controller.createNewGame();
        playerId = 0;

        // Gdx.input.setCursorCatched(true);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        for (Actor a : controller.allActors()) {
            stage.addActor(a);
        }

        rotationSpeed = 0.5f;
        maxZoom = 0.2f;

        InputProcessor inputProcessor = (InputProcessor) new InputCore(this);
        Gdx.input.setInputProcessor(inputProcessor);

        createMasks();
    }

    public Controller getController(){
        return controller;
    }

    /**
     * @return the camera
     */
    public static Camera getCamera() {
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
    }

    /**
     * {@summary Draw all thing that need to be draw during a game.}
     * 
     * @see com.badlogic.gdx.Screen#render(float)
     */
    @Override
    public void render(float delta) {
        handleInput(); // Done before draw to avoid some GUI glitch
        // ScreenUtils.clear(0.1f, 1f, 0f, 1);
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

        controller.updateActorVisibility(playerId);
        drawMasked();
        drawMasks();
    }

    private void createMasks() {
        areaVisible = new HashSet<Circle>();
        areaVisible.add(new Circle(camera.position.x,camera.position.y,300));
        // shapeRenderer = new ShapeRenderer();
        // shapeRenderer.setAutoShapeType(true);
        spriteBatch = new SpriteBatch();

        /* Apply the mask to our Pixmap. */
        Pixmap pixmap = applyMask(300);
        /* Load the pixel information of the Pixmap into a Texture for drawing. */
        masked = new Texture(pixmap);
    }

    private void drawMasks() {
        spriteBatch.begin();
        spriteBatch.draw(masked, 0, 0);
        spriteBatch.end();
    }

    private void drawMasked() {
        stage.act(Gdx.graphics.getDeltaTime());// update actions are drawn here
        stage.draw();
        
    }

    private Pixmap applyMask(int radius) {
        Pixmap darkedArea = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        Pixmap toRemove = new Pixmap(darkedArea.getWidth(), darkedArea.getHeight(), Pixmap.Format.RGBA8888);
    
        /* This setting lets us overwrite the pixels' transparency. */ //it seem's to be useless.
        toRemove.setBlending(null);
    
        /* Ignore RGB values unless you want funky toRemoves, alpha is for the mask. */
        toRemove.setColor(new Color(1f, 1f, 1f, 1f));
    
        toRemove.fillCircle(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, radius);
    
        /* We can also define the mask by loading an image:
         * toRemove = new Pixmap(new FileHandle("image.png")); */
        int blackPixel = new Color(0.9f, 0f, 0f, 0f).toIntBits();
    
        /* Decide the color of each pixel. */
        for (int x = 0; x < toRemove.getWidth(); x++) {
            for (int y = 0; y < toRemove.getHeight(); y++) {
                if(toRemove.getPixel(x, y) == 0.0){
                    darkedArea.drawPixel(x, y, blackPixel);
                }
            }
        }
    
        return darkedArea;
    }




    /**
     * {@summary Handle user input &#38; mostly move camera.}<br>
     * Some input are handle on InputCore that allow more input handling.
     */
    private void handleInput() {
        // while debuging game close on escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            game.dispose();

        // TODO get a vector from mouse position & send it to controler to move the
        // player
        double moveY = 0;
        double moveX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveY += Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveY -= Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveX -= Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveX += Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            camera.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            camera.rotate(rotationSpeed, 0, 0, 1);
        }
        if (camera.zoom < maxZoom) {
            camera.zoom = maxZoom;
        }

        // TODO synchonise camera on creature insted of creature on camera.
        moveY *= 200;
        moveX *= 200;
        // if(moveX!=0)
        // System.out.println(camera.position);
        // tell the camera to update its matrices.
        camera.translate((int) moveX, (int) moveY, 0);
        controller.synchonisePlayerCreatureWithCamera(camera, playerId);
    }

    /**
     * {@summary Resize ViewPort when Screen is resize.}
     * 
     * @see com.badlogic.gdx.Screen#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }
}
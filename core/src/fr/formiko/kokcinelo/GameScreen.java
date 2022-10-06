package fr.formiko.kokcinelo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private final App game;
    private Viewport viewport;
    private Set<Rectangle> aphidSet;

    OrthographicCamera camera;
    private Texture ladybugImage;
    private Texture aphidImage;
    private Rectangle ladybug;
    private float rotationSpeed;
    private float maxZoom;

    public GameScreen(final App game) {
        this.game = game;
        Gdx.input.setCursorCatched(true);
        float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(Gdx.graphics.getWidth()*0.5f, Gdx.graphics.getHeight()*0.5f, 0);
        // camera.setToOrtho(false, 800, 480);
        viewport = new ScreenViewport(camera);
        ladybugImage = new Texture(Gdx.files.internal("images/ladybug.png"));
        ladybug = new Rectangle();
        ladybug.width = Gdx.graphics.getWidth() / 10;
        // keep racio
        ladybug.height = (ladybug.width * ladybugImage.getHeight()) / ladybugImage.getWidth();
        // center
        ladybug.x = (Gdx.graphics.getWidth() - ladybug.width) / 2;
        ladybug.y = (Gdx.graphics.getHeight() - ladybug.width) / 2;

        aphidSet = new HashSet<Rectangle>();
        aphidImage = new Texture(Gdx.files.internal("images/aphid.png"));
        Random ran = new Random();
        for (int i = 0; i < 10; i++) {
            Rectangle aphid = new Rectangle();
            aphid.width = Gdx.graphics.getWidth() / 50;
            // keep racio
            aphid.height = (aphid.width * aphidImage.getHeight()) / aphidImage.getWidth();
            // random location
            aphid.x = ran.nextInt((int) (Gdx.graphics.getWidth() - aphid.width));
            aphid.y = ran.nextInt((int) (Gdx.graphics.getHeight() - aphid.width));
            aphidSet.add(aphid);
        }
        // System.out.println(aphidSet);
        // System.out.println(ladybug);
        rotationSpeed = 0.5f;
        maxZoom = 0.2f;
    }

    public Camera getCamera(){ return camera;}

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        // clear the screen with a green color.
        ScreenUtils.clear(0.1f, 1f, 0f, 1);
        viewport.apply();
		handleInput();
        camera.update();
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        // draw images
        for (Rectangle aphid : aphidSet) {
            game.batch.draw(aphidImage, aphid.x, aphid.y, aphid.width, aphid.height);
        }
        game.batch.draw(ladybugImage, ladybug.x, ladybug.y, ladybug.width, ladybug.height);
		game.batch.end();
    }

    private void handleInput() {
        // DOING
        InputProcessor inputProcessor = (InputProcessor) new InputCore(this);
		Gdx.input.setInputProcessor(inputProcessor);



        // while debuging game close on escape
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            game.dispose();

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
        if(camera.zoom<maxZoom){
            camera.zoom=maxZoom;
        }

        moveY *=200;
        moveX *=200;
        // if(moveX!=0)
        //     System.out.println(camera.position);
        // tell the camera to update its matrices.
        camera.translate((int)moveX, (int)moveY, 0);
        ladybug.x = camera.position.x-ladybug.width/2;
        ladybug.y = camera.position.y-ladybug.height/2;
    }

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
package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.RedAnt;
import fr.formiko.kokcinelo.tools.KScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TraillerImage extends KScreen implements Screen {
    private Sprite sprite;
    private SpriteBatch spriteBatch;
    private Stage stage;

    public TraillerImage() {

        final float aphidSize = 0.35f * 3;
        final float antSize = 0.075f * 6;
        final float ladybugSize = 0.33f * 6;

        stage = new Stage();

        Creature c = new Ladybug();
        c.setZoom(ladybugSize);
        c.setRotation(-90);
        c.setCenter(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() / 2f);
        c.setMaxLifePoints(0);
        Creature c2 = new RedAnt();
        c2.setZoom(antSize);
        c2.setRotation(90);
        c2.setCenter(Gdx.graphics.getWidth() * 0.9f, Gdx.graphics.getHeight() / 2f);

        Aphid aphid = new Aphid();
        aphid.setZoom(aphidSize);
        aphid.setCenter(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        // sprite = Shapes.getCircledSprite(radius, App.GREEN, c, c2);
        spriteBatch = new SpriteBatch();
        MapActor map = new MapActor(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, App.GREEN, true);
        map.setScale(2f, 2f);
        stage.addActor(map);
        stage.addActor(aphid.getActor());
        stage.addActor(c.getActor());
        stage.addActor(c2.getActor());
    }

    @Override
    public void show() { // TODO Auto-generated method stub
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        stage.act();
        stage.draw();
        spriteBatch.end();
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

}

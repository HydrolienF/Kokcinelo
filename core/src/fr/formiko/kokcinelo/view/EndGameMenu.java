package fr.formiko.kokcinelo.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EndGameMenu implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;

    public EndGameMenu(SpriteBatch sb, int score, int maxScore){
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        // viewport = new FitViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, new OrthographicCamera());
        // viewport.setScreenBounds(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        stage = new Stage(viewport, sb);
        BitmapFont bmf = new BitmapFont();
        bmf.getData().setScale(3,3);
        Color col;
        if(score==maxScore){
            col = Color.GREEN;
        }else if(score>maxScore/2){
            col = Color.YELLOW;
        }else{
            col = Color.RED;
        }
        Label.LabelStyle style = new Label.LabelStyle(bmf, col);
        scoreLabel = new Label(score+"%", style);

        Table table = new Table();
        table.center();
        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);
        // table.setFillParent(true);
        table.add(scoreLabel).expandX();
        // TODO add a replay button.

        stage.addActor(table);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
}

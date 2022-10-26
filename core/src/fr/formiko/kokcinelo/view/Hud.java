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

// TODO implement Hud as https://github.com/BrentAureli/SuperMario/blob/dc48dcb6d5b25e8b3c22908df23a5328f13868f9/core/src/com/brentaureli/mariobros/Scenes/Hud.java
// & then add label that we need from GameState here

public class Hud implements Disposable {

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private Label timeLabel;
    private float timeCount;
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private Label countdownLabel;

    public Hud(SpriteBatch sb, int worldTimer){
        this.worldTimer = worldTimer;
        timeCount = 0;
        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);
        countdownLabel = new Label(String.format("", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel =new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        // timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        // table.add(timeLabel).expandX();//.padTop(10);
        // table.row();
        table.add(scoreLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%d", worldTimer));
            timeCount = 0;
        }
    }

    public void setPlayerScore(int value){
        scoreLabel.setText(String.format("%d", value));
    }

    public boolean isTimeUp() { return timeUp; }

    @Override
    public void dispose() {
        stage.dispose();
    }
    
}

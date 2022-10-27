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

/**
 * {@summary Game Hud that display player/game stats.}
 */
public class Hud implements Disposable {

    // Scene2D.ui Stage and its own Viewport for HUD
    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private float timeCount;
    private Integer worldTimer;
    /** true when the world timer reaches 0 */
    private boolean timeUp;
    private Label countdownLabel;

    public Hud(SpriteBatch sb, int worldTimer) {
        this.worldTimer = worldTimer;
        timeCount = 0;
        // setup the HUD viewport using a new camera seperate from our gamecam
        // define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        countdownLabel = new Label(String.format("", worldTimer), style);
        scoreLabel = new Label("", style);

        // define a table used to organize our hud's labels
        Table table = new Table();
        // Top-Align table
        table.top();
        // make the table fill the entire stage
        table.setFillParent(true);

        // table.add(timeLabel).expandX();//.padTop(10);
        // table.row();
        table.add(scoreLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    public Stage getStage() {
        return stage;
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%d", worldTimer));
            timeCount = 0;
        }
    }

    public void setPlayerScore(int value) {
        scoreLabel.setText(String.format("%d", value));
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

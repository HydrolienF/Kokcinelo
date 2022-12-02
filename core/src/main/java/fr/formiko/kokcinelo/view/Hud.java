package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
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
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class Hud implements Disposable {

    // Scene2D.ui Stage and its own Viewport for HUD
    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    // private float timeCount;
    // private Integer worldTimer;
    /** true when the world timer reaches 0 */
    private boolean timeUp;
    private Label countdownLabel;
    // private long startTime;
    private float gameTime;

    /**
     * {@summary Main constructor}
     * 
     * @param sb         SpriteBatch to use
     * @param worldTimer starting value of world timer
     */
    public Hud(SpriteBatch sb, int worldTimer) {
        // this.worldTimer = worldTimer;
        // timeCount = 0;
        // setup the HUD viewport using a new camera seperate from our gamecam
        // define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);
        BitmapFont bmf = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        Label.LabelStyle style = new Label.LabelStyle(bmf, Color.WHITE);
        countdownLabel = new Label("", style);
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
        App.log(0, "constructor", "new Hud: " + toString());
        // startTime = System.currentTimeMillis();
        gameTime = worldTimer;
    }

    public Stage getStage() { return stage; }
    public void setPlayerScore(int value) { scoreLabel.setText("" + value); }
    public float getGameTime() { return gameTime; }
    /**
     * {@summary Set the game time.}
     */
    public void setGameTime(float gameTime) {
        App.log(1, "gameTime=" + gameTime);
        this.gameTime = gameTime;
    }
    public boolean isTimeUp() { return timeUp; }

    /**
     * {@summary Update Component that need to be update on time.}
     * 
     * @param dt Delta time that occure since last update
     */
    public void update(float dt) {
        gameTime -= dt;
        countdownLabel.setText("" + (int) (gameTime + 0.1));
        if (gameTime < 0) {
            timeUp = true;
        }
        // timeCount = System.currentTimeMillis() - startTime;
        // if (timeCount > 0) {
        // if (worldTimer > 0) {
        // worldTimer--;
        // } else {
        // timeUp = true;
        // }
        // countdownLabel.setText("" + worldTimer);
        // timeCount = 0;
        // }
    }

    @Override
    public void dispose() { stage.dispose(); }

}

package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.tools.Fonts;
import fr.formiko.kokcinelo.tools.KScreen;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
public class Hud extends KScreen implements Disposable {

    // Scene2D.ui Stage and its own Viewport for HUD
    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private Label insectCountLabel;
    // private float timeCount;
    // private Integer worldTimer;
    /** true when the world timer reaches 0 */
    private boolean timeUp;
    private Label countdownLabel;
    // private long startTime;
    private float gameTime;
    private int maxValue;

    /**
     * {@summary Main constructor}
     * 
     * @param sb SpriteBatch to use
     */
    public Hud(SpriteBatch sb) {
        this.maxValue = Controller.getController().getNumberOfAphids();
        // timeCount = 0;
        // setup the HUD viewport using a new camera seperate from our gamecam
        // define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);
        countdownLabel = new Label("", skin);
        scoreLabel = new Label("", skin);
        insectCountLabel = new Label("", skin, "emoji");

        // define a table used to organize our hud's labels
        Table table = new Table();
        // Top-Align table
        table.top();
        // make the table fill the entire stage
        table.setFillParent(true);

        // table.add(timeLabel).expandX();//.padTop(10);
        // table.row();
        table.add(scoreLabel).expandX();
        table.add(insectCountLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
        App.log(0, "constructor", "new Hud: " + toString());
        // startTime = System.currentTimeMillis();
        gameTime = -1;
        setPlayerScore(0);
    }

    public Stage getStage() { return stage; }
    public void setPlayerScore(int value) { scoreLabel.setText(value + "/" + maxValue); }
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
        Map<Class<? extends Creature>, Integer> map = Controller.getController().getInsectList();
        StringBuilder sb = new StringBuilder();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
            try {
                sb.append(e.getValue()).append(Fonts.getIcon(e.getKey())).append(" ");
            } catch (Exception ex) {
                sb.append(e.getValue()).append(e.getKey().getName()).append(" ");
            }
        });
        insectCountLabel.setText(sb.toString());
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

package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.tools.Fonts;
import fr.formiko.kokcinelo.tools.KScreen;
import fr.formiko.usual.g;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@summary Game Hud that display player/game stats.}
 * 
 * @author Hydrolien
 * @version 2.3
 * @since 0.1
 */
public class Hud extends KScreen implements Disposable {

    // Scene2D.ui Stage and its own Viewport for HUD
    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private Label insectCountLabel;
    private @Null CreatureActionProgressBar creatureActionProgressBar;
    // private float timeCount;
    // private Integer worldTimer;
    /** true when the world timer reaches 0 */
    private boolean timeUp;
    private Label countdownLabel;
    // private long startTime;
    private float gameTime;
    private int maxValue;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor}
     * 
     * @param sb SpriteBatch to use
     */
    public Hud(SpriteBatch sb) {
        this.maxValue = Controller.getController().getGameState().getMaxScore();
        // timeCount = 0;
        // setup the HUD viewport using a new camera seperate from our gamecam
        // define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Actor closeButton = getCloseButton(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (App.isWithCloseButton()) {
            stage.addActor(closeButton);
        }

        stage.addActor(createTopTable(Gdx.graphics.getWidth() - closeButton.getWidth()));
        stage.addActor(createBottomTable(Gdx.graphics.getWidth()));

        App.log(0, "constructor", "new Hud: " + toString());
        // startTime = System.currentTimeMillis();
        gameTime = -1;
        setPlayerScore(0);
    }
    /**
     * {@summary Create top labels for the Hud.}
     * 
     * @param width Width of the table
     * @return a Table with all top actors
     */
    private Table createTopTable(float width) {
        countdownLabel = new Label("", skin);
        scoreLabel = new Label("", skin);
        insectCountLabel = new Label("", skin, "emoji");

        // define a table used to organize our hud's labels
        Table topTable = new Table();
        // Top-Align table
        topTable.top();
        topTable.setSize(width, Gdx.graphics.getHeight());

        topTable.add(scoreLabel).expandX();
        topTable.add(insectCountLabel).expandX();
        topTable.add(countdownLabel).expandX();
        return topTable;
    }
    /**
     * {@summary Create bottom buton for the Hud.}
     * 
     * @param width Width of the table
     * @return a Table with all bottom actors
     */
    private Table createBottomTable(int width) {
        Table bottomTable = new Table();
        bottomTable.bottom();
        bottomTable.setSize(width, Gdx.graphics.getHeight());

        String spaceActionName = Controller.getController().getPlayerCreature().getSpaceActionName();
        if (spaceActionName != null) {
            creatureActionProgressBar = new CreatureActionProgressBar(width, height, new Color(0.5f, 0.5f, 0.5f, 1f),
                    new Color(0.5f, 0.5f, 0.5f, 0.2f), Color.BLACK, Color.GREEN, g.get(spaceActionName), skin);
            float capbWidth = width / 5f;
            creatureActionProgressBar.setSize(capbWidth, capbWidth / 7f);
            bottomTable.add(creatureActionProgressBar);
        }
        return bottomTable;
    }

    // GET SET -------------------------------------------------------------------
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


    // FUNCTIONS -----------------------------------------------------------------
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
        insectCountLabel.setText(Fonts.listOfCreatureToString(Controller.getController().getInsectList()));
        updateCreatureActionProgressBar();
    }

    @Override
    public void dispose() { stage.dispose(); }

    private void updateCreatureActionProgressBar() {
        if (creatureActionProgressBar != null) {
            creatureActionProgressBar.setProgress(Controller.getController().getPlayerCreature().getSpaceActionProgress());
        }
    }

}

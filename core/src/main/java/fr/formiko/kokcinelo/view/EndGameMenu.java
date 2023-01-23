package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.usual.g;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@summary Menu for the end of the game}
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.1
 */
public class EndGameMenu implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private Image replayButton;
    private Button mainMenuButton;
    private boolean haveWin;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Create a new end game menu.}
     * 
     * @param sb       Batch to use to draw
     * @param score    score that player reach
     * @param maxScore score that player may have reach
     * @param haveWin  true if player have win the game
     */
    public EndGameMenu(SpriteBatch sb, int score, int maxScore, boolean haveWin) {
        this.haveWin = haveWin;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        // viewport = new FitViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, new OrthographicCamera());
        // viewport.setScreenBounds(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2,
        // Gdx.graphics.getHeight()/2);
        stage = new Stage(viewport, sb);
        BitmapFont bmf = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        // bmf.getData().setScale(3,3);

        // Label.LabelStyle style = new Label.LabelStyle(bmf, Color.WHITE);
        scoreLabel = new Label(score * 100 / maxScore + "%",
                new Label.LabelStyle(bmf, getColorFromPercent((double) (score) / (double) (maxScore))));
        Texture t = new Texture(Gdx.files.internal("images/icons/basic/replay.png"));
        replayButton = new Image(t);
        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Controller.getController().restartGame(); }
        });
        mainMenuButton = new TextButton(g.get("MainMenu"), MenuScreen.getDefautSkin());
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Controller.getController().createNewMenuScreen(); }
        });


        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 10);
        table.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() * 2 / 3);
        table.add(scoreLabel).expandX();
        table.add(mainMenuButton).expandX();
        table.add(replayButton).expandX();

        stage.addActor(table);

        App.log(0, "constructor", "new EndGameMenu: " + toString());
    }

    // GET SET -------------------------------------------------------------------
    public Stage getStage() { return stage; }
    @Override
    public void dispose() { stage.dispose(); }

    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Return color depending of haveWin or percent of score.}
     * 
     * @param percent score percent [0;1]
     * @return color
     */
    private Color getColorFromPercent(double percent) {
        if (haveWin) {
            return Color.GREEN;
        } else {
            return new Color(1f, (float) percent, 0f, 1f);
        }
    }
}

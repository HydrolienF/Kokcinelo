package fr.formiko.kokcinelo.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@summary Menu for the end of the game}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class EndGameMenu implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private ImageButton replayButton;
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
        Texture t = new Texture(Gdx.files.internal("images/" + "replay" + ".png"));
        // TODO change color of t to white.
        replayButton = new ImageButton(new TextureRegionDrawable(t));

        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        table.center();
        table.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
        table.add(scoreLabel).expandX();
        // TODO add a replay button.
        table.add(replayButton).expandX();

        stage.addActor(table);
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

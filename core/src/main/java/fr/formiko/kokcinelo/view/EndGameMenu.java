package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.tools.KScreen;
import fr.formiko.usual.g;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@summary Menu for the end of the game}
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 0.1
 */
public class EndGameMenu extends KScreen implements Disposable {

    private Stage stage;
    private Viewport viewport;
    private Label scoreLabel;
    private Image replayButton;
    private Label mainMenuButton;
    // private boolean haveWin;

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
        // this.haveWin = haveWin;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        // viewport = new FitViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, new OrthographicCamera());
        // viewport.setScreenBounds(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2,
        // Gdx.graphics.getHeight()/2);
        stage = new Stage(viewport, sb);

        replayButton = new Image(new Texture(Gdx.files.internal("images/icons/basic/replay.png")));
        replayButton.setColor(Color.WHITE);
        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Controller.getController().restartGame(); }
        });
        mainMenuButton = new Label(g.get("MainMenu"), skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Controller.getController().createNewMenuScreen(); }
        });

        int percent = score * 100 / maxScore;
        Label.LabelStyle ls = skin.get(Label.LabelStyle.class);
        LabelStyle style = new LabelStyle(ls.font, App.getColorFromPercent(percent));
        style.background = ls.background;
        skin.add("s" + percent, style);
        scoreLabel = new Label(score * 100 / maxScore + "%", skin, "s" + percent);


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
}

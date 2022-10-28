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
    private Label replayButton;
    private boolean haveWin;

    public EndGameMenu(SpriteBatch sb, int score, int maxScore, boolean haveWin) {
        this.haveWin = haveWin;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        // viewport = new FitViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, new OrthographicCamera());
        // viewport.setScreenBounds(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2,
        // Gdx.graphics.getHeight()/2);
        stage = new Stage(viewport, sb);
        BitmapFont bmf = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        // bmf.getData().setScale(3,3);

        Label.LabelStyle style = new Label.LabelStyle(bmf, Color.WHITE);
        scoreLabel = new Label(score + "%", new Label.LabelStyle(bmf, getColorFromPercent((double) (score) / (double) (maxScore))));
        replayButton = new Label("↻", style);

        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        table.center();
        table.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
        table.add(scoreLabel).expandX();
        // TODO add a replay button.
        table.add(replayButton).expandX();

        stage.addActor(table);
    }

    public Stage getStage() { return stage; }
    @Override
    public void dispose() { stage.dispose(); }

    private Color getColorFromPercent(double percent) {
        if (haveWin) {
            return Color.GREEN;
        } else if (percent > 0.5) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

}

package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.usual.g;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@summary Menu display when escape key is pressed.}
 * It contains link to many state of the game.
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 0.3
 */
public class EscapeGameMenu implements Disposable {
    private Stage stage;
    private static Skin skin;
    private static final List<String> keyList = List.of("restart", "endPartie", "backToMainMenu", "leaveKokcinelo", "resume");

    // CONSTRUCTORS ----------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     * Load skin. Add button. Add listener to button.
     * 
     * @param sb batch to draw the menu.
     */
    public EscapeGameMenu(SpriteBatch sb) {
        FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);

        int pad = Gdx.graphics.getWidth() / 192;

        Table table = new Table();
        // table.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 10);
        // table.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() * 2 / 3);

        if (skin == null) {
            skin = MenuScreen.getDefautSkin();
            for (String key : keyList) {
                ImageTextButtonStyle itbs = new ImageTextButtonStyle();
                itbs.font = skin.getFont("default");
                switch (key) {
                    case "restart":
                        itbs.fontColor = Color.YELLOW;
                        break;
                    case "endPartie":
                        itbs.fontColor = new Color(1f, 0.8f, 0f, 1);
                        break;
                    case "backToMainMenu":
                        itbs.fontColor = new Color(1f, 0.5f, 0f, 1);
                        break;
                    case "leaveKokcinelo":
                        itbs.fontColor = Color.RED;
                        break;
                    case "resume":
                        itbs.fontColor = Color.GREEN;
                        break;
                    default:
                        itbs.fontColor = Color.WHITE;
                        break;
                }
                itbs.imageUp = new TextureRegionDrawable(new Texture(Gdx.files.internal("images/icons/basic/" + key + ".png")))
                        .tint(itbs.fontColor);
                skin.add(key, itbs);
            }
        }
        for (String string : keyList) {
            ImageTextButton itb = new ImageTextButton(g.getM(string), skin, string);
            switch (string) {
                case "restart":
                    itb.addListener(new ClickListener() {
                        /**
                         * Restart game.
                         */
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Controller.getController().removeEscapeMenu();
                            Controller.getController().gameOver();
                            Controller.getController().restartGame();
                        }
                    });
                    break;
                case "backToMainMenu":
                    itb.addListener(new ClickListener() {
                        /**
                         * Go back to main menu.
                         */
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Controller.getController().removeEscapeMenu();
                            Controller.getController().gameOver();
                            Controller.getController().createNewMenuScreen();
                        }
                    });
                    break;
                case "endPartie":
                    itb.addListener(new ClickListener() {
                        /**
                         * End current game.
                         */
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Controller.getController().removeEscapeMenu();
                            Controller.getController().gameOver();
                        }
                    });
                    break;
                case "leaveKokcinelo":
                    itb.addListener(new ClickListener() {
                        /**
                         * Leave game.
                         */
                        @Override
                        public void clicked(InputEvent event, float x, float y) { Controller.getController().exitApp(); }
                    });
                    break;
                case "resume":
                    itb.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) { Controller.getController().pauseResume(); }
                    });
                    break;
                default:
                    App.log(2, "No listener for " + string + " button.");
                    break;
            }
            table.add(itb).pad(0, pad, 0, pad);
            table.row();
        }

        table.center();
        table.setBackground(Shapes.getWhiteBackground());
        table.pack();
        table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4, Align.center);

        stage.addActor(table);
    }
    public Stage getStage() { return stage; }
    @Override
    public void dispose() { stage.dispose(); }

}

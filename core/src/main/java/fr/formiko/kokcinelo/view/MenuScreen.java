package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.usual.g;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@summary The main menu screen.}
 * 
 * @see com.badlogic.gdx.Screen
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
public class MenuScreen implements Screen {
    private Stage stage;
    private SpriteBatch batch;
    private Skin skin;
    private InputMultiplexer inputMultiplexer;
    private final Label scoresLabel;
    private final Label levelDescription;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     */
    public MenuScreen() {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);

        skin = getDefautSkin();
        batch = new SpriteBatch();

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);

        final int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();

        Table centerTable = new Table();
        centerTable.setBounds(0, h / 2, w, h / 6);


        // TODO button should be a triangle image (draw on Pixmap or loaded from file)
        final TextButton button = new TextButton("Play", skin);
        button.setSize(100, 100);
        centerTable.add(button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { getController().endMenuScreen(); }
        });

        stage.addActor(getLevelButtonTable()); // need to be done before use getScoresText()

        scoresLabel = new Label("", skin);
        scoresLabel.setBounds(0, h * 2 / 3, w / 3, h / 3);
        scoresLabel.setAlignment(Align.center);
        scoresLabel.setWrap(true);

        levelDescription = new Label("", skin);
        levelDescription.setBounds(w * 2 / 3, h * 2 / 3, w / 3, h / 3);
        levelDescription.setAlignment(Align.center);
        levelDescription.setWrap(true);

        updateSelectedLevel(getLevelId());


        stage.addActor(centerTable);
        stage.addActor(scoresLabel);
        stage.addActor(levelDescription);
        // stage.setDebugAll(true);
        addProcessor(stage);
        App.log(0, "constructor", "new MenuScreen: " + toString());

    }

    // GET SET -------------------------------------------------------------------
    public Controller getController() { return Controller.getController(); }
    public Stage getStage() { return stage; }
    public void addProcessor(InputProcessor ip) { inputMultiplexer.addProcessor(ip); }
    public String getLevelId() { return LevelButton.getCheckedButton().getId(); }
    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Render the screen.}
     */
    @Override
    public void render(float delta) {
        // // TODO remove when menu will be ready.
        // if (false) {
        // Controller.getController().endMenuScreen();
        // return;
        // }
        ScreenUtils.clear(App.BLUE_BACKGROUND);
        stage.act(delta);
        stage.draw();
    }

    /**
     * {@summary Dispose all variable that need to be dispose to save memory.}
     * 
     * @see com.badlogic.gdx.Game#dispose()
     */
    @Override
    public void dispose() {
        App.log(0, "destructor", "dispose MenuScreen: " + toString());
        stage.dispose();
    }

    // TODO Auto-generated method stub
    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    /**
     * {@summary Handle user input.}<br>
     */
    private InputProcessor getInputProcessor() {
        InputProcessor inputProcessor = (InputProcessor) new InputProcessor() {

            @Override
            public boolean keyDown(int keycode) { return false; }
            /**
             * {@summary React to key pressed once.}
             * 
             * @param keycode the key pressed
             */
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
                    Controller.getController().endMenuScreen();
                }
                return true;
            }

            @Override
            public boolean keyTyped(char character) { return false; }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

            @Override
            public boolean mouseMoved(int screenX, int screenY) { return false; }

            @Override
            public boolean scrolled(float amountX, float amountY) { return false; }

        };
        return inputProcessor;
    }

    /**
     * @return A simple skin that menus use
     */
    public Skin getDefautSkin() {
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libGDX font under the name "default".
        skin.add("default", new BitmapFont(Gdx.files.internal("fonts/font.fnt")));

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        // textButtonStyle.up = skin.newDrawable("white", Color.GREEN);
        // textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        // textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        // textButtonStyle.over = skin.newDrawable("white", Color.RED);
        textButtonStyle.font = skin.getFont("default");
        // textButtonStyle.fontColor = Color.RED;
        skin.add("default", textButtonStyle);

        ButtonStyle buttonStyle = new ButtonStyle();
        skin.add("default", buttonStyle);


        skin.add("default", new LabelStyle(skin.getFont("default"), Color.BLACK));

        return skin;
    }

    // private ------------------------------------------------------------------------------------
    /**
     * Add the 9 level buttons as circle to fill all aviable space & preserve same space between buttons.
     * 
     * @return The level button table
     */
    private Table getLevelButtonTable() {
        LevelButton.clearList();
        final int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();

        Table levelButtonTable = new Table();
        levelButtonTable.setBounds(0, 0, w, h / 2);
        int buttonRadius = (int) levelButtonTable.getWidth() / 20;
        int buttonSize = buttonRadius * 2;
        int len = 5;

        int xFreeSpace = (int) levelButtonTable.getWidth() - (len * buttonSize);
        int xSpaceBetweenButton = xFreeSpace / (len + 1);

        int yFreeSpace = (int) levelButtonTable.getHeight() - (2 * buttonSize);
        int ySpaceBetweenButton = yFreeSpace / 3;

        for (int i = 0; i < len; i++) {
            LevelButton levelButtonK = new LevelButton(buttonRadius, skin, (i + 1) + "K");
            levelButtonK.setPosition(xSpaceBetweenButton + (xSpaceBetweenButton + buttonSize) * i,
                    levelButtonTable.getHeight() - ySpaceBetweenButton - buttonSize);
            if (i == 0) {
                levelButtonK.setChecked(true);
            } else {
                LevelButton levelButtonF = new LevelButton(buttonRadius, skin, (i + 1) + "F");
                levelButtonF.setPosition(xSpaceBetweenButton + (xSpaceBetweenButton + buttonSize) * i, ySpaceBetweenButton);
            }
        }
        for (LevelButton levelButton : LevelButton.getList()) {
            levelButtonTable.addActor(levelButton);
            levelButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) { updateSelectedLevel(levelButton.getId()); }
            });
        }


        // TODO draw button image as grey image + a black lok over it.


        return levelButtonTable;
    }

    /**
     * Update the label that depend of selected level.
     * 
     * @param levelId the selected level
     */
    private void updateSelectedLevel(String levelId) {
        scoresLabel.setText(getScoresText(levelId));
        levelDescription.setText(getLevelDescription(levelId));
    }
    /**
     * @param levelId the level id
     * @return A String with level name, last &#38; best score
     */
    private String getScoresText(String levelId) {
        return g.get("Level") + " " + levelIdToString(levelId) + "\n" + g.get("BestScore") + " : " + getController().getBestScore(levelId)
                + "%\n" + g.get("LastScore") + " : " + getController().getLastScore(levelId) + "%";
    }
    private String getLevelDescription(String levelId) { return g.get("DescriptionLevel" + levelId, g.get("CommingSoon")); }
    /**
     * @param levelId the level id
     * @return A String representing the level id.
     */
    private String levelIdToString(String levelId) {
        String r = "" + levelId.charAt(0) + " - ";
        if (levelId.charAt(1) == 'K') {
            r += "Kokcinelo";
        } else {
            r += "Formiko";
        }
        return r;
    }

}

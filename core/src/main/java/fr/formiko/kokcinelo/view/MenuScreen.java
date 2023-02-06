package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Level;
import fr.formiko.kokcinelo.tools.Files;
import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.usual.Chrono;
import fr.formiko.usual.g;
import java.util.Map;
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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
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
    private static Skin skin;
    private static Skin skinTitle;
    private InputMultiplexer inputMultiplexer;
    private final Label scoresLabel;
    private final Label levelNameLabel;
    private final Label levelDescription;
    private final Chrono chrono;
    private final int topSpace;
    private static final boolean backgroundLabelColored = true;
    private static Map<String, String> languageName;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     */
    public MenuScreen() {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);

        if (skin == null) {
            skin = getDefautSkin();
        }
        if (skinTitle == null) {
            skinTitle = getDefautSkin(40);
        }
        if (languageName == null) {
            languageName = Files.loadMapFromCSVFile("languages/languageList.csv", true);
            App.log(1, "" + languageName);
        }
        batch = new SpriteBatch();

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);

        final int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();
        topSpace = h * 40 / 100;
        int bottomSpace = h * 50 / 100;
        int bottomLinksSpace = h * 5 / 100;
        int centerSpace = h * 10 / 100;

        Table centerTable = new Table();
        centerTable.setBounds(0, bottomSpace, w, centerSpace);

        final Image playButton = new Image(new Texture(Gdx.files.internal("images/icons/basic/play.png")));
        playButton.setSize(centerTable.getHeight(), centerTable.getHeight());
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { getController().endMenuScreen(); }
        });
        playButton.setScaling(Scaling.fillY);
        centerTable.add(playButton).expand().fill();

        Image ladybug = new Image(new Texture(Gdx.files.internal("images/Creatures/ladybug flying.png")));
        ladybug.setScaling(Scaling.contain); // Do not distort the image
        ladybug.setBounds(w / 3, h - topSpace, w / 3, topSpace);

        stage.addActor(getLevelButtonTable(w, bottomSpace)); // need to be done before use getScoresText()


        levelNameLabel = new Label("", skinTitle);
        // levelNameLabel.setBounds(0, h - (topSpace / 2), w / 3, topSpace / 2);
        levelNameLabel.setAlignment(Align.center);
        // levelNameLabel.setWrap(true);

        scoresLabel = new Label("", skin);
        // scoresLabel.setBounds(0, h - topSpace, w / 3, topSpace / 2);
        scoresLabel.setAlignment(Align.center);
        // scoresLabel.setWrap(true);

        levelDescription = new Label("", skin);
        // levelDescription.setBounds(w * 2 / 3, h - topSpace, w / 3, topSpace);
        levelDescription.setSize(w / 3, topSpace);
        levelDescription.setAlignment(Align.center);
        levelDescription.setWrap(true);

        updateSelectedLevel(getLevelId());

        Table btable = getLinkButtonsTable(bottomLinksSpace);
        btable.setSize(bottomLinksSpace * 5, bottomLinksSpace);
        btable.setPosition(0, 0);
        stage.addActor(btable);


        stage.addActor(btable);
        stage.addActor(centerTable);
        stage.addActor(levelNameLabel);
        stage.addActor(scoresLabel);
        stage.addActor(levelDescription);
        stage.addActor(ladybug);
        // stage.setDebugAll(true);
        addProcessor(stage);

        chrono = new Chrono();
        chrono.start();
        App.log(0, "constructor", "new MenuScreen: " + toString());

    }

    // GET SET -------------------------------------------------------------------
    public Controller getController() { return Controller.getController(); }
    public Stage getStage() { return stage; }
    public void addProcessor(InputProcessor ip) { inputMultiplexer.addProcessor(ip); }
    public String getLevelId() { return LevelButton.getCheckedButton().getId(); }
    public Level getLevel() { return LevelButton.getCheckedButton().getLevel(); }
    // FUNCTIONS -----------------------------------------------------------------
    /**
     * {@summary Render the screen.}
     */
    @Override
    public void render(float delta) {
        // ScreenUtils.clear(App.BLUE_BACKGROUND);

        // draw blue sky gradient
        float secToCycle = 3 * 60 + 54;
        // float secToCycle = 10;
        chrono.updateDuree();
        float ligth = 1 - ((chrono.getDuree() % (secToCycle * 1000) / (secToCycle * 1000f))); // [0.5 ; 1]
        if (ligth < 0.5f) {
            ligth = 1f - ligth;
        }
        Shapes.drawSky(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), ligth);

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
     * @param fontSize size of the font
     * @return A simple skin that menus use
     */
    public static Skin getDefautSkin(int fontSize) {
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Noto_Sans/NotoSans-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = fontSize;
        // TODO load char from assets/bin/language.yml
        // TODO save all char from all translations files (assets/languages/**) in bin/language.yml when creating jar file.
        // TODO If bin/language.yml don't exist, caculate it.
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "ĉĝĥĵŝŭ" + "ĈĜĤĴŜŬ" + " ";
        BitmapFont bmf = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        // bmf.getData().markupEnabled = true; //Use to set color label by label

        // Store the default libGDX font under the name "default".
        skin.add("default", bmf);

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

        LabelStyle labelStyle = new LabelStyle(skin.getFont("default"), Color.BLACK);
        // set background
        if (backgroundLabelColored) {
            pixmap = new Pixmap(1, 1, Format.RGBA8888);
            pixmap.setColor(new Color(1f, 1f, 1f, 0.3f));
            pixmap.fill();
            labelStyle.background = new Image(new Texture(pixmap)).getDrawable();
            pixmap.dispose();
        }

        skin.add("default", labelStyle);
        // skin.add("default", new LabelStyle(skin.getFont("default"), null)); //Use to set color label by label

        return skin;
    }
    /***
     * @return A simple skin that menus use
     */
    public static Skin getDefautSkin() { return getDefautSkin(28); }

    // private ------------------------------------------------------------------------------------
    /**
     * Add the 9 level buttons as circle to fill all aviable space & preserve same space between buttons.
     * 
     * @return The level button table
     */
    private Table getLevelButtonTable(final int w, final int h) {
        LevelButton.clearList();

        Table levelButtonTable = new LevelButtonTable(w / 200);
        levelButtonTable.setBounds(0, 0, w, h);
        int buttonRadius = (int) levelButtonTable.getWidth() / 24;
        int buttonSize = buttonRadius * 2;
        int len = 5;

        int xFreeSpace = (int) levelButtonTable.getWidth() - (len * buttonSize);
        int xSpaceBetweenButton = xFreeSpace / (len + 1);

        int yFreeSpace = (int) levelButtonTable.getHeight() - (3 * buttonSize);
        int ySpaceBetweenButton = yFreeSpace / len;

        App.log(1, "Level.getLevelList() " + Level.getLevelList());

        LevelButton.resetUnlockedLevels();
        for (Level level : Level.getLevelList()) {
            LevelButton levelButton = new LevelButton(buttonRadius, skin, level.getId(), this);
            float y = 0;
            switch (level.getLetter()) {
            case "K":
                y = ySpaceBetweenButton * 2 + buttonSize;
                break;
            case "F":
                y = ySpaceBetweenButton;
                break;
            case "A":
                y = levelButtonTable.getHeight() - ySpaceBetweenButton - buttonSize;
                break;
            }
            levelButton.setPosition(xSpaceBetweenButton + (xSpaceBetweenButton + buttonSize) * (level.getNumber() - 1), y);
            if (level.getNumber() == 1) {
                levelButton.setChecked(true);
            }
            levelButtonTable.addActor(levelButton);

        }

        return levelButtonTable;
    }

    /**
     * Update the labels that depend of selected level.
     * 
     * @param levelId the selected level
     */
    public void updateSelectedLevel(String levelId) {
        levelNameLabel.setText(getLevelNameText(levelId));
        scoresLabel.setText(getScoresText(levelId));
        levelDescription.setText(getLevelDescription(levelId));
        updateLabels();
    }
    /**
     * Update the labels that depend of selected level.
     * 
     * @param levelId the selected level
     */
    public void updateOveredLevel(String levelId) {
        levelNameLabel.setText(getLevelNameText(levelId));
        scoresLabel.setText(getScoresText(levelId));
        levelDescription.setText(getLevelDescription(levelId));
        updateLabels();
    }
    /**
     * Update the labels location &#38; size.
     */
    private void updateLabels() {
        int maxLabelWidth = Gdx.graphics.getWidth() / 3;
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        scoresLabel.pack();
        levelNameLabel.pack();
        levelDescription.pack();
        levelDescription.setWidth(maxLabelWidth);
        scoresLabel.setPosition((maxLabelWidth - scoresLabel.getWidth()) / 2, h - topSpace / 2 - scoresLabel.getHeight());
        levelNameLabel.setPosition((maxLabelWidth - levelNameLabel.getWidth()) / 2, h - topSpace / 2);
        levelDescription.setPosition(w * 2 / 3 + (maxLabelWidth - levelDescription.getWidth()) / 2,
                Math.min(h - (topSpace + levelDescription.getHeight()) / 2, h - levelDescription.getHeight()));
    }


    // Private -------------------------------------------------------------------------------------
    /**
     * {@summary Return a table of web site link button.}
     * 
     * @return A table of web site link button
     */
    private Table getLinkButtonsTable(int size) {
        Table table = new Table();
        table.add(getClickableLink("basic/info", "https://github.com/HydrolienF/Kokcinelo#team", size, false));

        table.add(getClickableLink("homeWebSiteLink", "https://formiko.fr/kokcinelo", size, true));
        table.add(getClickableLink("discordLink", "https://discord.gg/vqvfGzf", size, true));
        // table.add(getClickableLink("reportBugLink", "https://formiko.fr/kokcinelo", size));
        table.add(getClickableLink("supportGameLink", "https://tipeee.com/formiko", size, true));

        Table langTable = new Table();
        for (String languageCode : App.SUPPORTED_LANGUAGE) {
            Label languageLabel = new Label(languageName.get(languageCode), skin);
            languageLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    langTable.remove();
                    App.setLanguage(languageCode);
                    updateSelectedLevel(getLevelId());
                }
            });
            langTable.add(languageLabel);
            langTable.row();
        }
        langTable.pack();
        langTable.setPosition((stage.getWidth() - langTable.getWidth()) / 2, (stage.getHeight() - langTable.getHeight()) / 2);
        Image flag = getClickableLink("basic/language", null, size, false);
        flag.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!langTable.remove()) {
                    stage.addActor(langTable);
                }
            }
        });
        table.add(flag);
        return table;
    }

    /**
     * {@summary Return a web site link button.}
     * 
     * @return A web site link button
     */
    private Image getClickableLink(String imageName, String url, int buttonSize, boolean outlined) {
        Texture t = resizeTexture("images/icons/" + imageName + ".png", buttonSize, outlined);
        Image b = new Image(t);
        if (url != null) {
            b.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) { Gdx.net.openURI(url); }
            });
        }
        b.setScaling(Scaling.fillY);
        return b;
    }

    /**
     * {@summary Return a resized texture.}
     * It is used to resize link button icons.
     * 
     * @return A resized texture
     */
    private Texture resizeTexture(String textureName, int size, boolean outlined) {
        Pixmap pixmapOut = Shapes.resize(new Pixmap(Gdx.files.internal(textureName)), size, size);
        if (outlined) {
            pixmapOut = Shapes.outLine(pixmapOut);
        }
        Texture texture = new Texture(pixmapOut);
        pixmapOut.dispose();
        return texture;
    }

    /**
     * @param levelId the level id
     * @return A String with level name, last &#38; best score
     */
    private String getScoresText(String levelId) {
        return g.get("BestScore") + " : " + getController().getBestScore(levelId) + "%\n" + g.get("LastScore") + " : "
                + getController().getLastScore(levelId) + "%";
    }
    /**
     * @param levelId the level id
     * @return A String with level name, last &#38; best score
     */
    private String getLevelNameText(String levelId) { return g.get("Level") + " " + levelIdToString(levelId); }
    private String getLevelDescription(String levelId) {
        String desc = g.get("DescriptionLevel" + levelId.substring(1, 2), "") + " " + g.get("DescriptionLevel" + levelId, "");
        if (App.isPlayableLevel(levelId)) {
            return desc;
        } else {
            return g.get("ComingSoon") + "\n" + desc;
        }
    }
    /**
     * @param levelId the level id
     * @return A String representing the level id.
     */
    private String levelIdToString(String levelId) {
        String r = "" + levelId.charAt(0) + " - ";
        if (levelId.charAt(1) == 'K') {
            r += "Kokcinelo";
        } else if (levelId.charAt(1) == 'F') {
            r += "Formiko";
        } else if (levelId.charAt(1) == 'A') {
            r += "Afido";
        }
        return r;
    }

}

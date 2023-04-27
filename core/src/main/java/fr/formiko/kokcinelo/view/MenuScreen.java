package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GreenAnt;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.LadybugSideView;
import fr.formiko.kokcinelo.model.Level;
import fr.formiko.kokcinelo.model.RedAnt;
import fr.formiko.kokcinelo.tools.Files;
import fr.formiko.kokcinelo.tools.KScreen;
import fr.formiko.kokcinelo.tools.KTexture;
import fr.formiko.kokcinelo.tools.Musics;
import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.usual.g;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@summary The main menu screen.}
 * 
 * @see com.badlogic.gdx.Screen
 * @author Hydrolien
 * @version 1.3
 * @since 0.2
 */
public class MenuScreen extends KScreen implements Screen {
    private Stage stage;
    private Stage backgroundStage;
    private SpriteBatch batch;
    private static Skin skin = getDefautSkin();
    private static Skin skinSmall = getDefautSkin(14);
    private static Skin skinTitle = getDefautSkin(40);
    private InputMultiplexer inputMultiplexer;
    private Label scoresLabel;
    private Label levelNameLabel;
    private Label levelDescription;
    private Label versionLabel;
    private int topSpace;
    private static final boolean backgroundLabelColored = true;
    private static String DEFAULT_CHARS;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private static List<Actor> creatureImages;
    private boolean playingVideo = false;
    private long timePlayingVideo;
    private int fullVideoTime = 1000;
    private float BACKGROUND_SPEED = 50;
    private OrthographicCamera cameraBc;

    // CONSTRUCTORS --------------------------------------------------------------
    /**
     * {@summary Main constructor.}
     */
    public MenuScreen() {
        super();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer);

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, batch);

        cameraBc = new OrthographicCamera();
        Viewport viewportBc = new ScreenViewport(cameraBc);
        backgroundStage = new Stage(viewportBc, batch);
        backgroundStage.addActor(new EnvironmentMenuScreen(cameraBc));

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
        long time = System.currentTimeMillis();
        ScreenUtils.clear(Color.BLACK);

        cameraBc.position.x += delta * BACKGROUND_SPEED;
        if (getLevel().getLetter().equals("K")) {
            Actor environement = backgroundStage.getRoot().findActor("environement");
            cameraBc.position.y = environement.getHeight() * environement.getScaleY() - Gdx.graphics.getHeight() / 2f;
            cameraBc.zoom = 1;
        } else {
            cameraBc.zoom = 0.3f;
            cameraBc.position.y = Gdx.graphics.getHeight() / 2f * cameraBc.zoom;
        }
        batch.setProjectionMatrix(cameraBc.combined);
        backgroundStage.act(delta);
        backgroundStage.draw();

        batch.setProjectionMatrix(camera.combined);
        for (Actor creature : creatureImages) { // Only show the creature of the current level.
            creature.setVisible(creature.getName() != null
                    && (creature.getName().equals(getLevel().getLetter()) || (creature.getName().startsWith(getLevel().getLetter())
                            && creature.getName().contains("" + getLevel().getNumber()))));
        }

        if (playingVideo) {
            hidingButtonAnimation(delta);
            // TODO last visible actor need to act while video is playing.
            if (timePlayingVideo < System.currentTimeMillis() - fullVideoTime) {
                Controller.getController().endMenuScreen();
            }
        }

        stage.act(delta);
        stage.draw();
        times.add((int) (System.currentTimeMillis() - time));
    }

    /**
     * {@summary Progressively hide buton.}
     */
    private void hidingButtonAnimation(float delta) {
        for (Actor actor : stage.getActors()) {
            if (!creatureImages.contains(actor)) {
                float modifY = 1000f * delta * (Gdx.graphics.getHeight() / 1080f);
                if (actor.getY() < Gdx.graphics.getHeight() / 2) {
                    modifY *= -1;
                }
                actor.setY(actor.getY() + modifY);
            }
        }
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
    public void resize(int width, int height) {
        if (!needResize(width, height))
            return;

        stage.clear();
        final int w = width; // = Gdx.graphics.getWidth();
        final int h = height; // = Gdx.graphics.getHeight();
        viewport.update(w, h, true);
        App.log(1, "MenuScreen have size: " + w + "x" + h);
        topSpace = h * 40 / 100;
        int bottomSpace = h * 50 / 100;
        int bottomLinksSpace = h * 5 / 100;
        int centerSpace = h * 10 / 100;

        Table centerTable = new Table();
        centerTable.setBounds(0, bottomSpace, w, centerSpace);

        centerTable.add(getPlayButton(centerTable.getHeight(), centerTable.getHeight())).expand();

        if (App.isWithCloseButton()) {
            stage.addActor(getCloseButton(w, h));
        }

        createCreatureImages(w, h);

        stage.addActor(getLevelButtonTable(w, bottomSpace)); // need to be done before use getScoresText()

        createLabels();
        levelDescription.setSize(w / 3, topSpace);
        updateSelectedLevel(getLevelId());

        versionLabel = new Label(App.getCurrentVersion(), skinSmall);
        versionLabel.setPosition(w - versionLabel.getWidth(), 0);

        Table btable = getLinkButtonsTable(bottomLinksSpace);
        btable.setSize(bottomLinksSpace * 5, bottomLinksSpace);
        btable.setPosition(0, 0);

        stage.addActor(btable);
        stage.addActor(levelNameLabel);
        stage.addActor(scoresLabel);
        stage.addActor(levelDescription);
        for (Actor creature : creatureImages) {
            stage.addActor(creature);
        }
        stage.addActor(centerTable);
        stage.addActor(versionLabel);

        stage.setDebugAll(Controller.isDebug());
        addProcessor(stage);
    }

    /**
     * {@summary Create images for all creature that can be play.}
     * It is used to display them walking or flying in the menu.
     */
    private void createCreatureImages(int w, int h) {
        creatureImages = new ArrayList<Actor>();
        for (Class<? extends Creature> creatureClass : List.of(RedAnt.class, GreenAnt.class, LadybugSideView.class)) {
            boolean needToRotate = false;
            Creature c = null;
            try {
                c = creatureClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                // TODO there is an error with .jar
                App.log(3, "MenuScreen", "Error while creating creature: " + e);
                continue;
            }
            MapItemActor cActor = c.getActor();
            int imageWidth;
            int imageHeigth;
            if (c instanceof Ant) {
                needToRotate = true;
                imageWidth = 3600;
                imageHeigth = 4800;
                if (c instanceof RedAnt) {
                    cActor.setName("F2");
                } else if (c instanceof GreenAnt) {
                    cActor.setName("F3");
                }
            } else if (c instanceof Ladybug) {
                if (c instanceof LadybugSideView) {
                    imageWidth = 1484;
                    imageHeigth = 926;
                } else {
                    imageWidth = 738;
                    imageHeigth = 536;
                }
                cActor.setName("K");
            } else if (c instanceof Aphid) {
                // TODO set imageWidth & imageHeigth
                imageWidth = 1000;
                imageHeigth = 1000;
                cActor.setName("A");
            } else {
                imageWidth = 1000;
                imageHeigth = 1000;
            }
            cActor.setBounds(w / 3, h - topSpace, w / 3, topSpace);
            if (needToRotate) {
                cActor.setOrigin(Align.center); // Don't work well with rotation of not square image.
                cActor.setRotation(-90);
            }
            c.setZoom(Math.min(cActor.getWidth() / imageHeigth, cActor.getHeight() / imageWidth));

            c.setCurrentSpeed(c.getMovingSpeed());
            c.setMaxLifePoints(0); // Don't show life bar

            creatureImages.add(cActor);
        }
    }

    /**
     * {@summary Create labels for the menu.}
     */
    private void createLabels() {
        levelNameLabel = new Label("", skinTitle);
        levelNameLabel.setAlignment(Align.center);

        scoresLabel = new Label("", skin);
        scoresLabel.setAlignment(Align.center);

        levelDescription = new Label("", skin);
        levelDescription.setAlignment(Align.center);
        levelDescription.setWrap(true);
    }

    /**
     * {@summary Create a close window button.}
     * 
     * @return a close window button.
     */
    private Actor getCloseButton(int w, int h) {
        final Image closeButton = new Image(new KTexture(Gdx.files.internal("images/icons/basic/endPartie.png")));
        closeButton.setSize(w / 40f, w / 40f);
        closeButton.setPosition(w - closeButton.getWidth() + 1, h - closeButton.getHeight() + 1);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { getController().exitApp(); }
        });
        return closeButton;
    }

    /**
     * {@summary Create a play button.}
     * 
     * @param pbWidth  Play button width.
     * @param pbHeight Play button height.
     * @return a play button.
     */
    private Actor getPlayButton(float pbWidth, float pbHeight) {
        final Image playButton = new Image(new KTexture(Gdx.files.internal("images/icons/basic/play.png")));
        playButton.setSize(pbWidth, pbHeight);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playingVideo) {
                    getController().endMenuScreen();
                } else {
                    startPlayingVideo();
                }
            }
        });
        playButton.setScaling(Scaling.fillY);
        return playButton;
    }

    @Override
    public void pause() { Musics.pause(); }

    @Override
    public void resume() { Musics.resume(); }

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
                    if (playingVideo) {
                        getController().endMenuScreen();
                    } else {
                        startPlayingVideo();
                    }
                }
                return true;
            }

            @Override
            public boolean keyTyped(char character) { return false; }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (playingVideo) {
                    getController().endMenuScreen();
                    return true;
                } else {
                    return false;
                }
            }

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
        if (DEFAULT_CHARS == null) {
            DEFAULT_CHARS = Files.loadUniqueCharFromTranslationFiles();
        }
        parameter.characters = DEFAULT_CHARS;// FreeTypeFontGenerator.DEFAULT_CHARS + eo char upper & lower case.
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
            labelStyle.background = Shapes.getWhiteBackground();
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
     * Update the labels that depend of overed level.
     * Currently, do the same as updateSelectedLevel.
     * 
     * @param levelId the selected level
     */
    public void updateOveredLevel(String levelId) { updateSelectedLevel(levelId); }

    public void startPlayingVideo() {
        playingVideo = true;
        timePlayingVideo = System.currentTimeMillis();
    }

    // Private -------------------------------------------------------------------------------------

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
        scoresLabel.setPosition((maxLabelWidth - scoresLabel.getWidth()) / 2, h - topSpace / 2f - scoresLabel.getHeight());
        levelNameLabel.setPosition((maxLabelWidth - levelNameLabel.getWidth()) / 2, h - topSpace / 2f);
        levelDescription.setPosition(w * 2f / 3f + (maxLabelWidth - levelDescription.getWidth()) / 2f,
                Math.min(h - (topSpace + levelDescription.getHeight()) / 2, h - levelDescription.getHeight()));
    }


    /**
     * {@summary Return a table of web site link button.}
     * It also have a swap language button.
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
        Label.LabelStyle ls = skin.get(Label.LabelStyle.class);
        int perRow = 1;
        if (App.SUPPORTED_LANGUAGES.size() > 10) {
            perRow = 4;
        }
        int k = 0;
        for (String languageCode : App.SUPPORTED_LANGUAGES) {
            String languageName = App.LANGUAGES_NAMES.get(languageCode);
            Integer percent = App.LANGUAGES_PERCENTAGES.get(languageCode);
            if (percent == null) {
                continue;
            }
            if (percent != 100) {
                languageName += " (" + percent + "%)";
            }
            LabelStyle style = new LabelStyle(ls.font, App.getColorFromPercent(percent));
            style.background = ls.background;
            skin.add("s" + percent, style);
            Label languageLabel = new Label(languageName, skin, "s" + percent);
            languageLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    langTable.remove();
                    App.setLanguage(languageCode);
                    updateSelectedLevel(getLevelId());
                }
            });
            langTable.add(languageLabel);
            k++;
            if (k % perRow == 0) {
                langTable.row();
            }
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

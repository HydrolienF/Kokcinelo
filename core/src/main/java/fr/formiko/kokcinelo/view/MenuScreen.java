package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.BigScoreAphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GreenAnt;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.LadybugSideView;
import fr.formiko.kokcinelo.model.Level;
import fr.formiko.kokcinelo.model.RedAnt;
import fr.formiko.kokcinelo.tools.Fonts;
import fr.formiko.kokcinelo.tools.KScreen;
import fr.formiko.kokcinelo.tools.KTexture;
import fr.formiko.kokcinelo.tools.Musics;
import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.kokcinelo.view.OptionsTable.OptionsTablesTypes;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
 * @version 2.3
 * @since 0.2
 */
public class MenuScreen extends KScreen implements Screen {
    private Stage stage;
    private Stage backgroundStage;
    private SpriteBatch batch;
    private InputMultiplexer inputMultiplexer;
    private Label scoresLabel;
    private Label levelNameLabel;
    private Label levelDescription;
    private int topSpace;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private static List<Actor> creatureImages;
    private boolean playingVideo = false;
    private long timePlayingVideo;
    private int fullVideoTime = 1000;
    private float backgroundSpeed = 50;
    private OrthographicCamera cameraBc;
    private Actor playButton;
    private List<OptionsTable> optionsTables = new ArrayList<>();
    private boolean isPause;
    private SelectBox<String> playerCreatureSelectBox;

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
        if (!isPause) {
            long time = System.currentTimeMillis();
            ScreenUtils.clear(Color.BLACK);

            if (getLevel().getLetter().equals("K")) {
                Actor environement = backgroundStage.getRoot().findActor("environement");
                cameraBc.position.y = environement.getHeight() * environement.getScaleY() - Gdx.graphics.getHeight() / 2f;
                cameraBc.zoom = 1;
                backgroundSpeed = 50;
            } else if (getLevel().getLetter().equals("F")) {
                cameraBc.zoom = 0.3f;
                cameraBc.position.y = Gdx.graphics.getHeight() / 2f * cameraBc.zoom;
                backgroundSpeed = 50;
            } else if (getLevel().getLetter().equals("A")) {
                cameraBc.zoom = 0.1f;
                cameraBc.position.y = Gdx.graphics.getHeight() / 2f * cameraBc.zoom;
                backgroundSpeed = 14;
            }
            cameraBc.position.x += delta * backgroundSpeed;
            batch.setProjectionMatrix(cameraBc.combined);
            backgroundStage.act(delta);
            backgroundStage.draw();

            batch.setProjectionMatrix(camera.combined);
            for (Actor creatureImage : creatureImages) { // Only show the creature of the current level.
                // We use startsWith instead of equals because the class of the ladybug is LadybugSideView.
                creatureImage.setVisible(creatureImage.getName().startsWith(getLevel().getPlayerCreatureClass().getName()));
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
    }

    /**
     * {@summary Progressively hide buton.}
     */
    private void hidingButtonAnimation(float delta) {
        for (Actor actor : stage.getActors()) {
            if (!creatureImages.contains(actor)) {
                float modifY = 1000f * delta * getRacioHeight();
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

    @Override
    public void show() {
        // Noting special to do when the screen is shown.
    }

    @Override
    public void resize(int width, int height) {
        if (!needResize(width, height))
            return;
        App.saveSizeInOptions(width, height);

        stage.clear();
        // Gdx.graphics.getWidth() may be outdated here.
        final int w = width;
        final int h = height;
        viewport.update(w, h, true);
        App.log(1, "MenuScreen have size: " + w + "x" + h);
        topSpace = h * 40 / 100;
        int bottomSpace = h * 50 / 100;
        int bottomLinksSpace = h * 5 / 100;
        int centerSpace = h * 10 / 100;

        Table centerTable = new Table();
        centerTable.setBounds(0, bottomSpace, w, centerSpace);

        playButton = initPlayButton(centerTable.getHeight(), centerTable.getHeight());
        centerTable.add(playButton).expand();

        if (App.isWithCloseButton()) {
            stage.addActor(getCloseButton(w, h));
        }

        createCreatureImages(w, h, topSpace);

        stage.addActor(getLevelButtonTable(w, bottomSpace)); // need to be done before use getScoresText()

        createLabels();
        levelDescription.setSize(w / 3f, topSpace);
        updateSelectedLevel(getLevelId());

        Label versionLabel = new Label(Fonts.DEFAULT_COLOR + App.getCurrentVersion(), skinSmall);
        versionLabel.setPosition(w - versionLabel.getWidth(), 0);

        Table btable = getLinkButtonsTable(bottomLinksSpace);
        btable.setSize(bottomLinksSpace * 7, bottomLinksSpace);
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
    private static void createCreatureImages(int w, int h, int topSpace) {
        // final Map<Class<? extends Creature>, Vector2> imageSize = Map.of(Ant.class, new Vector2(3600, 4800));
        creatureImages = new ArrayList<>();
        for (Class<? extends Creature> creatureClass : List.of(RedAnt.class, GreenAnt.class, LadybugSideView.class, BigScoreAphid.class)) {
            Creature c = null;
            try {
                c = creatureClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                App.log(3, "MenuScreen", "Error while creating creature: " + e);
                continue;
            }
            MapItemActor cActor = c.getActor();
            cActor.setName(creatureClass.getName());
            int imageWidth;
            int imageHeight;
            if (c instanceof Ant) {
                imageWidth = 3600;
                imageHeight = 4800;
            } else if (c instanceof Ladybug) {
                if (c instanceof LadybugSideView) {
                    imageWidth = 1484;
                    imageHeight = 926;
                } else {
                    imageWidth = 738;
                    imageHeight = 536;
                }
            } else if (c instanceof Aphid) {
                imageWidth = 2000;
                imageHeight = 2000;
            } else {
                imageWidth = 1000;
                imageHeight = 1000;
            }
            cActor.setBounds(w / 3f, (float) h - topSpace, w / 3f, topSpace);
            if (!(c instanceof LadybugSideView)) { // Need to rotate
                cActor.setOrigin(Align.center); // Don't work well with rotation of not square image.
                cActor.setRotation(-90);
            }
            c.setZoom(Math.min(cActor.getWidth() / imageHeight, cActor.getHeight() / imageWidth));

            c.setCurrentSpeed(c.getMovingSpeed() * c.getDefaultMoveFrontSpeed() / 0.6f);
            c.setMaxLifePoints(0); // Don't show life bar
            c.setCollectedFrequency(-1); // to make sure it's not with honeydew

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
     * {@summary Create a play button.}
     * 
     * @param pbWidth  Play button width.
     * @param pbHeight Play button height.
     * @return a play button.
     */
    private Actor initPlayButton(float pbWidth, float pbHeight) {
        final Image playButton = new Image(new KTexture(Gdx.files.internal("images/icons/basic/play.png")));
        playButton.setSize(pbWidth, pbHeight);
        playButton.setColor(Color.GREEN);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { playButtonClick(); }
        });
        playButton.setScaling(Scaling.fillY);
        return playButton;
    }
    public void playButtonClick() {
        if (playingVideo) {
            getController().endMenuScreen();
        } else {
            startPlayingVideo();
        }
    }

    @Override
    public void pause() {
        Musics.pause();
        isPause = true;
    }

    @Override
    public void resume() {
        Musics.resume();
        isPause = false;
    }

    @Override
    public void hide() {
        // Nothing to do when hiding the screen.
    }

    /**
     * {@summary Handle user input.}
     */
    private InputProcessor getInputProcessor() {
        return new InputProcessor() {

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
                    keyPressedEscapeSpaceEnter();
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

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }

        };
    }
    /**
     * {@summary React to key pressed once when it's Escape or Space or Enter.}
     */
    private void keyPressedEscapeSpaceEnter() {
        if (playingVideo) { // skip video
            getController().endMenuScreen();
        } else if (optionsTables.stream().anyMatch(Actor::isVisible)) {
            // restart game
            if (optionsTables.stream().filter(OptionsTable::isVisible).anyMatch(ot -> ot.getType() == OptionsTablesTypes.RESTART)) {
                Controller.getController().restartFullGame();
            } else if (optionsTables.stream().anyMatch(OptionsTable::isRequireRestart)) { // ask for restart Kokcinelo
                optionsTables.stream().forEach(ot -> ot.setRequireRestart(false));
                setCenterActorVisible(
                        optionsTables.stream().filter(ot -> ot.getType() == OptionsTablesTypes.RESTART).findFirst().orElse(null));
            } else { // hide options
                setCenterActorVisible();
            }
        } else { // launch a new game
            startPlayingVideo();
        }
    }

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
        float buttonRadius = Math.min(levelButtonTable.getWidth() / 20f, levelButtonTable.getHeight() / 7f);
        float buttonSize = buttonRadius * 2;
        int len = 5;

        float xFreeSpace = levelButtonTable.getWidth() - (len * buttonSize);
        float xSpaceBetweenButton = xFreeSpace / (len + 1);

        float yFreeSpace = levelButtonTable.getHeight() - (3 * buttonSize);
        float ySpaceBetweenButton = yFreeSpace / len;

        App.log(1, "Level.getLevelList() " + Level.getLevelList());

        LevelButton.resetUnlockedLevels();
        for (Level level : Level.getLevelList().stream().sorted((l1, l2) -> l2.getDrawPriority() - l1.getDrawPriority())
                .collect(Collectors.toList())) {
            LevelButton levelButton = new LevelButton((int) buttonRadius, skin, level.getId(), this);
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
                default:
                    App.log(2, "Level " + level.getId() + " has no letter that match with a row");
                    break;
            }
            levelButton.setPosition((int) xSpaceBetweenButton + (xSpaceBetweenButton + buttonSize) * (level.getNumber() - 1), (int) y);
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
        updateOveredLevel(levelId);
        displayPlayerCreatureClassList(getLevel().getPlayerCreatureClasses().size() > 1);
    }
    /**
     * Update the labels that depend of overed level.
     * 
     * @param levelId the selected level
     */
    public void updateOveredLevel(String levelId) {
        levelNameLabel.setText(getLevelNameText(levelId));
        if (App.isPlayableLevel(levelId)) {
            levelNameLabel.setText(levelNameLabel.getText() + "\n" + getCreatureList(levelId));
        }
        scoresLabel.setText(getScoresText(levelId));
        levelDescription.setText(getLevelDescription(levelId));
        updateLabels();
    }

    private void displayPlayerCreatureClassList(boolean visible) {
        getLevel().setPlayerCreatureClassIndex(0);
        if (playerCreatureSelectBox == null) {
            playerCreatureSelectBox = new SelectBox<>(skinTitle);
            stage.addActor(playerCreatureSelectBox);
        }
        playerCreatureSelectBox.setVisible(visible);
        if (visible) {
            playerCreatureSelectBox.setItems(getLevel().getPlayerCreatureClasses().stream().map(Fonts::getIcon).toArray(String[]::new));
            playerCreatureSelectBox.pack();
            playerCreatureSelectBox.setPosition(levelDescription.getX() - playerCreatureSelectBox.getWidth(),
                    levelDescription.getY() + playerCreatureSelectBox.getHeight());
            playerCreatureSelectBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    getLevel().setPlayerCreatureClassIndex(playerCreatureSelectBox.getSelectedIndex());
                }
            });
        }
    }

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
        table.add(getClickableLink("supportGameLink", "https://tipeee.com/formiko", size, true));

        OptionsTable.setSkins(skin, skinTitle);
        table.add(getOptionsButton(size, OptionsTablesTypes.LANGUAGES, "language"));
        table.add(getOptionsButton(size, OptionsTablesTypes.AUDIO, "music"));
        table.add(getOptionsButton(size, OptionsTablesTypes.GRAPHICS, "screen"));
        OptionsTable restartTable = new OptionsTable(this, OptionsTablesTypes.RESTART);
        stage.addActor(restartTable);
        optionsTables.add(restartTable);
        return table;
    }

    /**
     * {@summary Return a clickable image that open options menu.}
     * 
     * @param size the size of the image
     * @return a clickable image that open options menu
     */
    private Image getOptionsButton(int size, OptionsTablesTypes type, String iconName) {
        OptionsTable optionsTable = new OptionsTable(this, type);
        stage.addActor(optionsTable);
        optionsTables.add(optionsTable);
        Image options = getClickableLink("basic/" + iconName, null, size, false);
        options.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (optionsTable.isVisible()) {
                    setCenterActorVisible();
                } else {
                    setCenterActorVisible(optionsTable);
                }
            }
        });
        return options;
    }


    /**
     * Set the only visible center actor.
     * 
     * @param actor the actor to set visible
     */
    void setCenterActorVisible(Actor actor) {
        if (actor == null) {
            setCenterActorVisible();
        } else {
            for (OptionsTable optionsTable : optionsTables) {
                optionsTable.setVisible(optionsTable.equals(actor));
            }
            playButton.setVisible(playButton.equals(actor));
        }
    }
    void setCenterActorVisible() { setCenterActorVisible(playButton); }

    /** Change language of the game */
    public void setLanguage(String languageCode) {
        App.setLanguage(languageCode);
        updateSelectedLevel(getLevelId()); // update levels Strings
        optionsTables.stream().forEach(OptionsTable::uninitialize); // reset option so that options string will be reloaded
    }

    /**
     * {@summary Return a web site link button.}
     * 
     * @return A web site link button
     */
    private Image getClickableLink(String imageName, String url, int buttonSize, boolean outlined) {
        Image b = new Image(resizeTexture("images/icons/" + imageName + ".png", buttonSize, outlined));
        if (imageName.contains("basic")) {
            b.setColor(Color.BLACK);
        }
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
        return Fonts.getTranslation("BestScore") + " : " + getController().getBestScore(levelId) + "%\n" + Fonts.getTranslation("LastScore")
                + " : " + getController().getLastScore(levelId) + "%";
    }
    /**
     * @param levelId the level id
     * @return A String with level name, last &#38; best score
     */
    private String getLevelNameText(String levelId) { return Fonts.getTranslation("Level") + " " + levelIdToString(levelId); }
    private String getLevelDescription(String levelId) {
        String desc = Fonts.getTranslationWidthDefault("DescriptionLevel" + levelId.substring(1, 2), "") + " "
                + Fonts.getTranslationWidthDefault("DescriptionLevel" + levelId, "");
        if (App.isPlayableLevel(levelId)) {
            return desc;
        } else {
            return Fonts.getTranslation("ComingSoon") + "\n" + desc;
        }
    }
    private String getCreatureList(String levelId) {
        return Fonts.listOfCreatureToString(Level.getLevel(levelId).getCreaturesToSpawn(), 4);
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

package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.tools.IntTextField;
import fr.formiko.kokcinelo.tools.KScreen;
import fr.formiko.kokcinelo.tools.Musics;
import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.usual.g;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * {@summary Options table.}
 * 
 * @author Hydrolien
 * @version 2.4
 * @since 2.4
 */
public class OptionsTable extends Table {
    enum OptionsTablesTypes {
        AUDIO, LANGUAGES, GRAPHICS, RESTART;
    }
    private static final String OPTIONS_STYLE = "optionsTableStyle";
    private static final String OPTIONS_STYLE_TITLE = "optionsTableStyleTitle";
    private final MenuScreen menuScreen;
    private static Skin skin;
    private final float padSize;
    private final OptionsTablesTypes type;
    private boolean initialized = false;
    private boolean requireRestart = false;

    /**
     * {@summary Create an empty OptionsTable.}
     */
    public OptionsTable(MenuScreen menuScreen, OptionsTablesTypes type) {
        super();
        this.menuScreen = menuScreen;
        this.type = type;
        this.padSize = 10 * KScreen.getRacio();
        setVisible(false);
    }

    /**
     * {@summary Initialize all item needed for the options.}
     */
    public void init() {
        setBackground(Shapes.getWhiteBackground());

        switch (type) {
            case AUDIO:
                initAudio();
                break;
            case LANGUAGES:
                initLanguages();
                break;
            case GRAPHICS:
                initGraphics();
                break;
            case RESTART:
                initRestart();
                break;
        }

        pack();
        setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Align.center);
        initialized = true;
        App.log(0, "constructor", "init OptionsTable: " + type);
    }

    public static void setSkins(Skin skin, Skin skinTitle) {
        OptionsTable.skin = skin;
        LabelStyle ls = new LabelStyle(skin.get(Label.LabelStyle.class).font, null);
        ls.fontColor = Color.BLACK;
        skin.add(OPTIONS_STYLE, ls);
        ls = new LabelStyle(skinTitle.get(Label.LabelStyle.class).font, null);
        ls.fontColor = Color.BLACK;
        skin.add(OPTIONS_STYLE_TITLE, ls);
    }

    /** Create audio options table */
    private void initAudio() {
        addTitleLable("Audio").colspan(2);
        row();
        addLabel("MusicVolume");
        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeMusicSlider.setValue(App.getOptionsMap().getMusicVolume());
        volumeMusicSlider.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                App.getOptionsMap().setMusicVolume(volumeMusicSlider.getValue());
                App.playSound("clicOn");
                Musics.setVolume(1f); // It only works if the music have 1 as default volume else we need to stop & restart music.
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) { touchDown(event, x, y, pointer, 0); }
        });
        add(volumeMusicSlider).pad(0, padSize, 0, padSize);
        row();
        addLabel("SoundVolume");
        final Slider volumeSoundSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSoundSlider.setValue(App.getOptionsMap().getSoundVolume());
        volumeSoundSlider.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                App.getOptionsMap().setSoundVolume(volumeSoundSlider.getValue());
                App.playSound("clicOn");
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) { touchDown(event, x, y, pointer, 0); }
        });
        add(volumeSoundSlider).pad(0, padSize, 0, padSize);
    }

    /** Create languages options table */
    private void initLanguages() {
        Label.LabelStyle ls = skin.get(Label.LabelStyle.class);
        int perRow = 1;
        if (App.SUPPORTED_LANGUAGES.size() > 10) {
            perRow = 4;
        }
        addTitleLable("Language").colspan(perRow);
        row();
        int k = 0;
        // for each language create a clickable label to switch to it language
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
            // style.background = ls.background;
            skin.add("s" + percent, style);
            Label languageLabel = new Label(languageName, skin, "s" + percent);
            languageLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    menuScreen.setCenterActorVisible();
                    App.setLanguage(languageCode);
                    menuScreen.updateSelectedLevel(menuScreen.getLevelId());
                }
            });
            addLabel(languageLabel);
            k++;
            if (k % perRow == 0) {
                row();
            }
        }
    }

    /** Create graphics options table */
    private void initGraphics() {
        addTitleLable("Video").colspan(2);
        row();

        int tfWidth = 4 * KScreen.FONT_SIZE; // It should be enoth for 4 char text (or less).
        final TextField widthTextField = new IntTextField(App.getOptionsMap().getScreenWidth(), skin, tfWidth, Align.center, i -> {
            App.getOptionsMap().setScreenWidth(i);
            requireRestart = true;
        });

        final TextField heightTextField = new IntTextField(App.getOptionsMap().getScreenHeight(), skin, tfWidth, Align.center, i -> {
            App.getOptionsMap().setScreenHeight(i);
            requireRestart = true;
        });

        final SelectBox<String> displayModeSelectBox = new SelectBox<>(skin);
        displayModeSelectBox.setItems(App.getOptionsMap().getListOfDisplayMode().toArray(new String[3]));
        displayModeSelectBox.setSelectedIndex(App.getOptionsMap().getDisplayMode());
        displayModeSelectBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { App.playSound("clicOn"); }
        });
        displayModeSelectBox.getScrollPane().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                requireRestart = true;
                App.getOptionsMap().setDisplayMode(displayModeSelectBox.getSelectedIndex());
                updateWidthHeightTextFieldVisibility(widthTextField, heightTextField);
                App.playSound("clicOff");
            }
        });
        // displayModeSelectBox.setAlignment(Align.center);
        displayModeSelectBox.setAlignment(Align.center);
        updateWidthHeightTextFieldVisibility(widthTextField, heightTextField);

        add(displayModeSelectBox).pad(0, padSize, 0, padSize).colspan(2);
        row();
        add(widthTextField).pad(0, padSize, 0, padSize);
        add(heightTextField).pad(0, padSize, 0, padSize);
        row();

        // TODO add field for max fps
    }
    private void updateWidthHeightTextFieldVisibility(TextField widthTextField, TextField heightTextField) {
        final boolean visible = App.getOptionsMap().getDisplayMode() >= 2;
        widthTextField.setVisible(visible);
        heightTextField.setVisible(visible);
    }

    /** Create a ask restart dialog */
    private void initRestart() {
        addTitleLable("RestartFullGame");
        row();
        addLabel("RestartFullGameMessage");
        row();
        TextButton restartButton = new TextButton(g.get("RestartFullGame"), skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Controller.getController().restartFullGame(); }
        });
        add(restartButton);
    }

    /**
     * Initialize the table by lazy when it's visible.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && !initialized) {
            init();
        }
        // if (requireRestart) {
        // requireRestart = false;
        // // TODO ask to restart app to apply changes
        // }
    }
    public OptionsTablesTypes getType() { return type; }
    public boolean isRequireRestart() { return requireRestart; }
    public void setRequireRestart(boolean requireRestart) { this.requireRestart = requireRestart; }


    private Cell<Label> addTitleLable(String text) { return addLabel(new Label(g.get(text), skin, OPTIONS_STYLE_TITLE)); }
    private Cell<Label> addLabel(String text) { return addLabel(new Label(g.get(text), skin, OPTIONS_STYLE)); }
    private Cell<Label> addLabel(Label label) { return add(label).pad(0, padSize, 0, padSize); }
}

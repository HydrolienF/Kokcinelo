package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * {@summary Options table.}
 */
public class OptionsTable extends Table {
    enum OptionsTablesTypes {
        AUDIO, LANGUAGES, GRAPHICS;
    }
    private static final String OPTIONS_STYLE = "optionsTableStyle";
    private final MenuScreen menuScreen;
    private final Skin skin;
    private final float padSize;
    private final OptionsTablesTypes type;

    /**
     * {@summary Initialize all item needed for the options.}
     */
    public OptionsTable(MenuScreen menuScreen, Skin skin, OptionsTablesTypes type) {
        super();
        this.menuScreen = menuScreen;
        this.skin = skin;
        this.type = type;
        setBackground(Shapes.getWhiteBackground());
        padSize = 10 * KScreen.getRacio();

        LabelStyle ls = new LabelStyle(skin.get(Label.LabelStyle.class).font, null);
        ls.fontColor = Color.BLACK;
        skin.add(OPTIONS_STYLE, ls);

        if (type == OptionsTablesTypes.AUDIO) {
            initAudio();
        } else if (type == OptionsTablesTypes.LANGUAGES) {
            initLanguages();
        } else if (type == OptionsTablesTypes.GRAPHICS) {
            initGraphics();
        }

        pack();
        setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Align.center);
    }

    /** Create audio options table */
    private void initAudio() {
        addLabel("Audio").colspan(2);
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
        addLabel("Video").colspan(2);
        row();
    }

    private Cell<Label> addLabel(String text) { return addLabel(new Label(g.get(text), skin, OPTIONS_STYLE)); }
    private Cell<Label> addLabel(Label label) { return add(label).pad(0, padSize, 0, padSize); }
}

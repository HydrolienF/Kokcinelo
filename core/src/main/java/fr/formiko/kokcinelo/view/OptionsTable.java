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
    private static final String OPTIONS_STYLE = "optionsTableStyle";
    private final Skin skin;
    private final float padSize;

    /**
     * {@summary Initialize all item needed for the options.}
     */
    public OptionsTable(Skin skin) {
        super();
        this.skin = skin;
        setBackground(Shapes.getWhiteBackground());
        padSize = 10 * KScreen.getRacio();

        LabelStyle ls = new LabelStyle(skin.get(Label.LabelStyle.class).font, null);
        ls.fontColor = Color.BLACK;
        skin.add(OPTIONS_STYLE, ls);

        addLabel("Options").colspan(2);
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
        row();

        pack();
        setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Align.center);
    }

    private Cell<Label> addLabel(String text) { return add(new Label(g.get(text), skin, OPTIONS_STYLE)).pad(0, padSize, 0, padSize); }
}

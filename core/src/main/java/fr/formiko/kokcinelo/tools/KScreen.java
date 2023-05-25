package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * {@summary Screen class with fiew more funtion than the one provide by libGDX.}
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 1.2
 */
public class KScreen {
    protected int width;
    protected int height;
    protected List<Integer> times;
    public static final int FONT_SIZE = 28;
    protected static Skin skin;
    protected static Skin skinSmall;
    protected static Skin skinTitle;
    private static final String DEFAULT_STYLE = "default";

    /**
     * {@summary Initialize collections that need to be initialize.}
     */
    public KScreen() {
        times = new LinkedList<>(); // LinkedList because many add and few get.
        if (skin == null) {
            skin = getDefautSkin();
            skinSmall = getDefautSkin(FONT_SIZE * getRacio() * 0.5f);
            skinTitle = getDefautSkin(FONT_SIZE * getRacio() * 1.6f);
        }
    }


    public static float getRacioWidth() { return Gdx.graphics != null ? Gdx.graphics.getWidth() / 1920f : 1f; }
    public static float getRacioHeight() { return Gdx.graphics != null ? Gdx.graphics.getHeight() / 1080f : 1f; }
    public static float getRacio() { return java.lang.Math.min(getRacioWidth(), getRacioHeight()); }
    public static float getFPSRacio() { return Gdx.graphics != null ? Gdx.graphics.getDeltaTime() * 60f : 1f; }


    /**
     * {@summary Return true if size have change.}
     * 
     * @param width  new width of the screen.
     * @param height new height of the screen.
     * @return true if the screen need to be resized.
     */
    public boolean needResize(int width, int height) {
        if ((width == 0 && height == 0) || (width == this.width && height == this.height))
            return false;
        this.width = width;
        this.height = height;
        return true;
    }
    /**
     * {@summary Display performances stats in log.}
     */
    public void displayPerf() {
        IntSummaryStatistics stats = times.stream().mapToInt(Integer::intValue).summaryStatistics();
        App.log(1, "PERFORMANCES",
                getClass().getSimpleName() + ": " + stats.getAverage() + " ms in average. (max: " + stats.getMax() + " ms)");
    }

    /**
     * @param fontSize size of the font
     * @return A simple skin that menus use
     */
    public static Skin getDefautSkin(float fontSize) {
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        BitmapFont bmf = Fonts.getDefaultFont(fontSize);

        // bmf.getData().markupEnabled = true; //Use to set color label by label

        // Store the default libGDX font under the name DEFAULT_STYLE.
        skin.add(DEFAULT_STYLE, bmf);
        skin.add("emoji", Fonts.getDefaultFont(fontSize, true));

        // Configure a TextButtonStyle and name it DEFAULT_STYLE. Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont(DEFAULT_STYLE);
        skin.add(DEFAULT_STYLE, textButtonStyle);

        ButtonStyle buttonStyle = new ButtonStyle();
        skin.add(DEFAULT_STYLE, buttonStyle);

        LabelStyle labelStyle = new LabelStyle(skin.getFont(DEFAULT_STYLE), null);
        LabelStyle labelStyleEmoji = new LabelStyle(skin.getFont("emoji"), null);
        // set background
        labelStyle.background = Shapes.getWhiteBackground();
        labelStyleEmoji.background = Shapes.getWhiteBackground();
        labelStyle.fontColor = Color.BLACK;

        skin.add(DEFAULT_STYLE, labelStyle);
        skin.add("emoji", labelStyleEmoji);
        // skin.add(DEFAULT_STYLE, new LabelStyle(skin.getFont(DEFAULT_STYLE), null)); //Use to set color label by label

        skin.add("default-horizontal", getSliderStyle(fontSize));
        skin.add(DEFAULT_STYLE, getScrollPaneStyle());
        skin.add(DEFAULT_STYLE, getListStyle(skin));
        skin.add(DEFAULT_STYLE, getSelectBoxStyle(skin));
        skin.add(DEFAULT_STYLE, getTextFieldStyle(skin));

        return skin;
    }
    /***
     * @return A simple skin that menus use
     */
    public static Skin getDefautSkin() { return getDefautSkin(FONT_SIZE * getRacio()); }

    /**
     * @return A slider style
     */
    private static SliderStyle getSliderStyle(float fontSize) {
        int borderWidth = (int) (fontSize / 8f);
        Drawable background = Shapes.getOveredRectangle((int) (100f * getRacioWidth()), (int) (fontSize / 2f), Color.GRAY, borderWidth,
                Color.BLACK);
        Drawable knob = Shapes.getRectangle((int) (fontSize / 3f), (int) (fontSize), Color.ORANGE);
        return new SliderStyle(background, knob);
    }
    private static ScrollPaneStyle getScrollPaneStyle() {
        return new ScrollPaneStyle(Shapes.getRectangle(1, 1, new Color(1f, 1f, 1f, 0.8f)), null, null, null, null);
    }
    private static ListStyle getListStyle(Skin skin) {
        return new ListStyle(skin.getFont(DEFAULT_STYLE), Color.BLACK, Color.BLACK, Shapes.getRectangle(1, 1, Color.ORANGE));
    }
    private static SelectBoxStyle getSelectBoxStyle(Skin skin) {
        return new SelectBoxStyle(skin.getFont(DEFAULT_STYLE), Color.BLACK, Shapes.getRectangle(1, 1, Color.WHITE),
                skin.get(ScrollPaneStyle.class), skin.get(ListStyle.class));
    }
    private static TextFieldStyle getTextFieldStyle(Skin skin) {
        return new TextFieldStyle(skin.getFont(DEFAULT_STYLE), Color.BLACK, Shapes.getRectangle(1, 1, Color.BLACK),
                Shapes.getRectangle(1, 1, Color.GRAY), Shapes.getRectangle(1, 1, Color.WHITE));
    }

    /**
     * {@summary Create a close window button.}
     * 
     * @return a close window button.
     */
    protected Actor getCloseButton(int w, int h) {
        final Image closeButton = new Image(new KTexture(Gdx.files.internal("images/icons/basic/endPartie.png")));
        closeButton.setColor(Color.RED);
        closeButton.setSize(w / 40f, w / 40f);
        closeButton.setPosition(w - closeButton.getWidth() + 1, h - closeButton.getHeight() + 1);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { Controller.getController().exitApp(); }
        });
        return closeButton;
    }
}

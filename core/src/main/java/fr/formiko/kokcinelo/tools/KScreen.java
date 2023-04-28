package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

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
    private static String DEFAULT_CHARS;
    private static final boolean backgroundLabelColored = true;
    public static final int FONT_SIZE = 28;
    protected static Skin skin = getDefautSkin();
    protected static Skin skinSmall = getDefautSkin(FONT_SIZE * getRacio() * 0.5f);
    protected static Skin skinTitle = getDefautSkin(FONT_SIZE * getRacio() * 1.6f);

    /**
     * {@summary Initialize collections that need to be initialize.}
     */
    public KScreen() {
        times = new LinkedList<>(); // LinkedList because many add and few get.
    }


    public static float getRacioWidth() { return Gdx.graphics.getWidth() / 1920f; }
    public static float getRacioHeight() { return Gdx.graphics.getHeight() / 1080f; }
    public static float getRacio() { return java.lang.Math.min(getRacioWidth(), getRacioHeight()); }


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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Noto_Sans/NotoSans-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = (int) fontSize;
        if (DEFAULT_CHARS == null) {
            DEFAULT_CHARS = Files.loadUniqueCharFromTranslationFiles();
        }
        parameter.characters = DEFAULT_CHARS;// FreeTypeFontGenerator.DEFAULT_CHARS + every char in the translation files.
        BitmapFont bmf = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        // bmf.getData().markupEnabled = true; //Use to set color label by label

        // Store the default libGDX font under the name "default".
        skin.add("default", bmf);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont("default");
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
    public static Skin getDefautSkin() { return getDefautSkin(FONT_SIZE * getRacio()); }
}

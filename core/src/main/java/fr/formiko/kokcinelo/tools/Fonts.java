package fr.formiko.kokcinelo.tools;

import fr.formiko.usual.g;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.EmojiSupport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * {@summary Class that provide a way to load font.}
 */
public class Fonts extends BitmapFont {
    private static String DEFAULT_CHARS;
    private static EmojiSupport emojiSupport;

    /**
     * {@summary Load the default font.}
     * 
     * @param fontSize size of the font.
     * @return the default font.
     */
    public static BitmapFont getDefaultFont(float fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Noto_Sans/NotoSans-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = (int) fontSize;
        if (DEFAULT_CHARS == null) {
            DEFAULT_CHARS = Files.loadUniqueCharFromTranslationFiles();
        }
        parameter.characters = DEFAULT_CHARS;// FreeTypeFontGenerator.DEFAULT_CHARS + every char in the translation files.
        BitmapFont bmf = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        if (emojiSupport == null) {
            emojiSupport = new EmojiSupport();
            emojiSupport.load(Gdx.files.internal("fonts/icons.atlas"));
        }
        emojiSupport.addEmojisToFont(bmf);

        return bmf;
    }

    // public static String filterEmojis(String s) { return s; }
    public static String filterEmojis(String s) { return emojiSupport.filterEmojis(s); }
    public static String getTranslation(String key) { return filterEmojis(g.get(key)); }
    public static String getTranslation(String key, String sDefault) { return filterEmojis(g.get(key, sDefault)); }

}

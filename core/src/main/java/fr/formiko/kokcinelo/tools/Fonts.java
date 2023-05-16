package fr.formiko.kokcinelo.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * {@summary Class that provide a way to load font.}
 */
public class Fonts extends BitmapFont {
    private static String DEFAULT_CHARS;

    /**
     * {@summary Load the default font.}
     * 
     * @param fontSize size of the font.
     * @return the default font.
     */
    public static BitmapFont getDefaultFont(float fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Noto_Sans/NotoSans-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = (int) fontSize;
        if (DEFAULT_CHARS == null) {
            DEFAULT_CHARS = Files.loadUniqueCharFromTranslationFiles();
        }
        parameter.characters = DEFAULT_CHARS;// FreeTypeFontGenerator.DEFAULT_CHARS + every char in the translation files.
        BitmapFont bmf = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        return bmf;
    }
}

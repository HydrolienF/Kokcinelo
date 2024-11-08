package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.BigScoreAphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.FlyingAphid;
import fr.formiko.kokcinelo.model.FlyingBigScoreAphid;
import fr.formiko.kokcinelo.model.GreenAnt;
import fr.formiko.kokcinelo.model.HealthAphid;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.RedAnt;
import fr.formiko.kokcinelo.model.ScoreAphid;
import fr.formiko.kokcinelo.model.SpeedAphid;
import fr.formiko.usual.g;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.EmojiSupport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * {@summary Class that provide a way to load font.}
 * 
 * @author Hydrolien
 * @version 2.4
 * @since 2.4
 */
public class Fonts extends BitmapFont {
    public static final String DEFAULT_COLOR = "[#000000]";
    private static String DEFAULT_CHARS;
    private static EmojiSupport emojiSupport;
    private static final Map<Class<? extends Creature>, String> icons;
    private static Map<Class<? extends Creature>, String> iconsTransformed;
    static {
        icons = new java.util.HashMap<>();
        icons.put(Ant.class, "🐜");
        icons.put(Ladybug.class, "🐞");
        icons.put(Aphid.class, "🦗");
        icons.put(RedAnt.class, "🕷");
        icons.put(GreenAnt.class, "🦂");
        icons.put(SpeedAphid.class, "🟦");
        icons.put(HealthAphid.class, "🟥");
        icons.put(ScoreAphid.class, "🟧");
        icons.put(BigScoreAphid.class, "🟧");
        icons.put(FlyingBigScoreAphid.class, "🔶");
        icons.put(FlyingAphid.class, "🟪");
    }

    /**
     * {@summary Load the default font.}
     * 
     * @param fontSize size of the font.
     * @return the default font.
     */
    public static BitmapFont getDefaultFont(float fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Noto_Sans/NotoSans-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.color = Color.WHITE;
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
            iconsTransformed = icons.entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, e -> filterEmojis(e.getValue())));
        }
        emojiSupport.addEmojisToFont(bmf);

        return bmf;
    }

    public static String filterEmojis(String s) { return emojiSupport.filterEmojis(s, DEFAULT_COLOR); }
    public static String filterEmojis(String s, String color) { return emojiSupport.filterEmojis(s, color); }
    public static String getTranslation(String key) { return filterEmojis(g.get(key)); }
    public static String getTranslation(String key, String color) { return filterEmojis(g.get(key), color); }
    public static String getTranslationWidthDefault(String key, String sDefault) { return filterEmojis(g.get(key, sDefault)); }
    public static String getTranslationWidthDefault(String key, String sDefault, String color) {
        return filterEmojis(g.get(key, sDefault), color);
    }

    public static String getIcon(Class<? extends Creature> c) { return iconsTransformed.get(c); }


    public static String listOfCreatureToString(Map<Class<? extends Creature>, Integer> map, int splitEveryXCreature) {
        AtomicInteger count = new AtomicInteger(0);
        StringBuilder sb = new StringBuilder();
        map.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(Creature.ORDER.indexOf(e1.getKey()), Creature.ORDER.indexOf(e2.getKey())))
                .forEach(e -> {
                    if (!sb.isEmpty()) {
                        if (count.incrementAndGet() >= splitEveryXCreature) {
                            sb.append("\n");
                            count.set(0);
                        } else {
                            sb.append("   ");
                        }
                    } else {
                        sb.append(DEFAULT_COLOR);
                    }
                    sb.append(e.getValue()).append(Fonts.getIcon(e.getKey()));
                });
        return sb.toString();
    }
    public static String listOfCreatureToString(Map<Class<? extends Creature>, Integer> map) {
        return listOfCreatureToString(map, Integer.MAX_VALUE);
    }
}

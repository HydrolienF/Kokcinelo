package com.badlogic.gdx.graphics.g2d;

import fr.formiko.kokcinelo.App;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StringBuilder;

/*
 * cf https://github.com/DavidPDev/gdx-EmojiSupport
 * Current version : 1.2
 * MIT License
 * 
 * Copyright (c) 2020 DavidPDev
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * {@summary Helper class to support emojis in libGDX.}
 */
public class EmojiSupport {
    private static final char START_CHAR = 0xE000; // Starting replacement-chars (private use area)
    private List<Texture> textures; // If multiple pages of emojis
    private Map<Integer, EmojiRegionIndex> regions; // Maps unicode emoji -> injected index

    /**
     * Load emojis from a file.
     * 
     * @param fileHandle file where the emojis are located.
     */
    public void load(FileHandle fileHandle) {
        // Default Linear filter (sometimes Nearest looks better with emojis)
        load(fileHandle, Texture.TextureFilter.Linear);
    }
    /**
     * Load emojis from a file.
     * 
     * @param fileHandle    file where the emojis are located.
     * @param textureFilter filter to apply to the emojis texture.
     */
    public void load(FileHandle fileHandle, Texture.TextureFilter textureFilter) {
        TextureAtlas textureAtlas = new TextureAtlas(fileHandle); // Emojis texture atlas
        textures = new ArrayList<>();
        for (Texture t : textureAtlas.getTextures()) {
            t.setFilter(textureFilter, textureFilter);
            textures.add(t);
        }

        regions = new HashMap<>();
        Array<TextureAtlas.AtlasRegion> regs = textureAtlas.getRegions();
        for (int i = 0; i < regs.size; i++) {
            try {
                int unicodeCode = Integer.parseInt(regs.get(i).name, 16);
                int page = 0;
                if (textures.size() > 1) { // Multi pages emojis, we must find the page where it's located
                    for (int j = 0; j < textures.size(); j++) {
                        if (textures.get(j).hashCode() == regs.get(i).getTexture().hashCode()) {
                            page = j;
                            break;
                        }
                    }
                }
                regions.put(unicodeCode, new EmojiRegionIndex(i, page, regs.get(i)));
            } catch (Exception e) {
                // Maybe error in name (not hex integer)
                throw new GdxRuntimeException("Invalid emoji (Not valid Hex code): " + regs.get(i).name);
            }
        }
    }
    /**
     * Add emojis to a font.
     * 
     * @param bitmapFont font to add emojis to.
     */
    public void addEmojisToFont(BitmapFont bitmapFont) {
        // 1- Add New TextureRegion
        Array<TextureRegion> regs = bitmapFont.getRegions();
        int pageLast = regs.size;
        for (Texture t : textures)
            regs.add(new TextureAtlas.AtlasRegion(t, 0, 0, t.getWidth(), t.getHeight()));

        // 2- Add all emoji glyphs to font
        int size = (int) (bitmapFont.getData().lineHeight / bitmapFont.getData().scaleY);
        for (EmojiRegionIndex entry : regions.values()) {
            App.log(0, "new emoji");
            char ch = (char) (START_CHAR + entry.index);
            BitmapFont.Glyph glyph = bitmapFont.getData().getGlyph(ch);
            if (glyph == null) { // Add Emoji as new Glyph (only if not exists in font)
                glyph = new BitmapFont.Glyph();
                glyph.id = ch;
                glyph.srcX = 0;
                glyph.srcY = 0;
                glyph.width = size;
                glyph.height = size;
                glyph.u = entry.atlasRegion.getU();
                glyph.v = entry.atlasRegion.getV2(); // Inverted y-axis (?)
                glyph.u2 = entry.atlasRegion.getU2();
                glyph.v2 = entry.atlasRegion.getV();
                glyph.xoffset = 0;
                glyph.yoffset = -size;
                glyph.xadvance = size;
                glyph.kerning = null;
                glyph.fixedWidth = true;
                glyph.page = pageLast + entry.page;
                bitmapFont.getData().setGlyph(ch, glyph);
            }
        }
    }
    /**
     * Filter a string to replace emojis with its index.
     * 
     * @param str string to filter.
     * @return filtered string.
     */
    public String filterEmojis(String str) { // Translates str replacing emojis with its index

        if (str == null || str.length() == 0)
            return str;

        int length = str.length();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (i < length) {
            char ch = str.charAt(i);
            boolean isCharSurrogate = (ch >= '\uD800' && ch <= '\uDFFF'); // Special 2-chars surrogates (uses two chars)
            boolean isCharVariations = (ch >= '\uFE00' && ch <= '\uFE0F'); // Special char for skin-variations (omit)
            int codePoint = str.codePointAt(i);
            EmojiRegionIndex eri = regions.get(codePoint);
            if (eri != null)
                sb.append((char) (START_CHAR + eri.index)); // Add found emoji
            else if (!isCharSurrogate && !isCharVariations)
                sb.append(ch); // Exclude special chars
            i += isCharSurrogate ? 2 : 1; // Surrogate chars use 2 characters
        }
        return sb.toString();
    }

    /**
     * Tools class to store emojis region data.
     */
    private class EmojiRegionIndex {
        int index; // Index starting from START_CHAR
        int page; // Multiple pages of emojis
        TextureAtlas.AtlasRegion atlasRegion; // Region data
        EmojiRegionIndex(int i, int p, TextureAtlas.AtlasRegion al) {
            index = i;
            page = p;
            atlasRegion = al;
        }
    }
}

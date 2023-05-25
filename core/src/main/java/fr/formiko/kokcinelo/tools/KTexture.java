package fr.formiko.kokcinelo.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 * {@summary Texture with high quality.}
 * MipMap is needed to have a good quality when the texture is scaled a lot smaller than the original size.
 * Texture.TextureFilter.MipMapLinearLinear is the best quality.
 * Mipmap may create issues with texure atlas (as spine output). To avoid create 2^n width &#38; height texture.
 * 
 * @author Hydrolien
 * @version 1.1
 * @since 1.1
 */
public class KTexture extends Texture {
    /**
     * {@summary Create good quality texture with mipmap.}
     * 
     * @param file file to load texture from.
     */
    public KTexture(FileHandle file) {
        super(file, true);
        setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }
    /**
     * {@summary Create good quality texture with mipmap.}
     * 
     * @param pixmap pixmap to create texture from.
     */
    public KTexture(Pixmap pixmap) {
        super(pixmap, true);
        setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }
}

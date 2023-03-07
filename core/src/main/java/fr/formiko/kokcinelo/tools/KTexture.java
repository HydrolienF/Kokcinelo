package fr.formiko.kokcinelo.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class KTexture extends Texture {
    public KTexture(FileHandle file) {
        super(file, true);
        setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        // super(file);
        // setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
}

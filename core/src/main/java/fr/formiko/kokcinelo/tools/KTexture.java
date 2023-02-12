package fr.formiko.kokcinelo.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class KTexture extends Texture {
    public KTexture(FileHandle file) {
        // super(file, true);
        // setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        super(file);
        setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
    }
}

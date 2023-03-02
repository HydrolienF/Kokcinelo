package fr.formiko.kokcinelo.tools;

public class KScreen {
    protected int width;
    protected int height;
    public boolean needResize(int width, int height) {
        if ((width == 0 && height == 0) || (width == this.width && height == this.height))
            return false;
        this.width = width;
        this.height = height;
        return true;
    }
}

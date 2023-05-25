package fr.formiko.kokcinelo;

/**
 * {@summary Native provide custom function for OS dependent things.}
 */
public interface Native {
    public void toFront();
    public void exit(int code);
}

/**
 * {@summary NullNative is a native implementation that do nothing.}
 */
class NullNative implements Native {
    @Override
    public void toFront() {
        // do nothing
    }
    public void exit(int code) {
        // do nothing
    }
}

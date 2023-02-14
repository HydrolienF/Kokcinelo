package fr.formiko.kokcinelo;

public interface Native {
    public void toFront();
}

class NullNative implements Native {
    @Override
    public void toFront() {}
}
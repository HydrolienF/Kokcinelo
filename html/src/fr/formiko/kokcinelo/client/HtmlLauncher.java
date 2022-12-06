package fr.formiko.kokcinelo.client;

import fr.formiko.kokcinelo.App;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        // Resizable application, uses available space in browser
        return new GwtApplicationConfiguration(true);
        // Fixed size application:
        // return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener createApplicationListener() { return new App(); }
    // @Override
    // public Preloader.PreloaderCallback getPreloaderCallback() { return createPreloaderPanel(GWT.getHostPageBaseURL() +
    // "preloadlogo.png"); }

    // @Override
    // protected void adjustMeterPanel(Panel meterPanel, Style meterStyle) {
    // meterPanel.setStyleName("gdx-meter");
    // meterPanel.addStyleName("nostripes");
    // meterStyle.setProperty("backgroundColor", "#ffffff");
    // meterStyle.setProperty("backgroundImage", "none");
    // }
}
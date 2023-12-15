package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.model.KOptionsMap;
import fr.formiko.usual.Os;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
/**
 * {@summary Launch for Windows, MacOs &#38; linux.}
 * 
 * @author Hydrolien
 * @version 1.0
 * @since 0.1
 */
public class DesktopLauncher {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].replace("-", "").equalsIgnoreCase("version")) {
            try {
                InputStream is = DesktopLauncher.class.getClassLoader().getResourceAsStream("version.md");
                String version = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
                        .collect(Collectors.joining("\n"));
                System.out.println(version);
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Fail to get version in DesktopLauncher.");
            }
        }

        Gdx.files = new Lwjgl3Files();
        KOptionsMap options = Controller.loadOptions();
        App.log(1, "options : " + options);
        int displayMode = options.getDisplayMode();

        // if (Os.getOs().isLinux()) { // TODO to remove
        //     displayMode = 0;
        // }

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(options.getMaxFps()); // 0 = no limite
        switch (displayMode) {
            default:
            case 0: // Real fullscreen
                config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
                App.setWithCloseButton(true);
                break;
            case 1: // windowed fullscreen
                config.setResizable(false);
                config.setDecorated(false);
                config.setMaximized(true); // create issues on linux. cf https://github.com/libgdx/libgdx/issues/7089
                App.setWithCloseButton(true);
                break;
            case 2: // windowed
                int screenWidth = options.getScreenWidth();
                int screenHeight = options.getScreenHeight();
                if (screenWidth > 0 && screenHeight > 0) {
                    App.log(1, "Start on windowed mode : " + screenWidth + "x" + screenHeight + ".");
                    config.setWindowedMode(screenWidth, screenHeight);
                    break;
                } else {
                    config.setMaximized(true);
                }
        }
        // config.setResizable(false);
        config.useVsync(true);
        config.setTitle("Kokcinelo");
        config.setWindowIcon("images/icons/appIcon.png");
        // config.setWindowListener(getWindowListenerThatPauseOnFucusLost());
        App game = new App(args, new DesktopNative());
        new Lwjgl3Application(game, config);
    }

    /**
     * {@summary Provide a Lwjgl3WindowListener that pause the game when focus is lost.}
     * 
     * @return a Lwjgl3WindowListener
     */
    public static Lwjgl3WindowListener getWindowListenerThatPauseOnFucusLost() {
        return new Lwjgl3WindowListener() {
            @Override
            public void focusLost() {
                App.log(1, "focusLost");
                Controller.getController().getScreen().pause();
            }
            @Override
            public void focusGained() {
                App.log(1, "focusGained");
                Controller.getController().getScreen().resume();
            }
            @Override
            public void filesDropped(String[] files) { App.log(1, "filesDropped : " + files); }
            @Override
            public boolean closeRequested() {
                App.log(1, "closeRequested");
                return true;
            }
            @Override
            public void iconified(boolean isIconified) { App.log(1, "iconified : " + isIconified); }
            @Override
            public void maximized(boolean isMaximized) { App.log(1, "maximized : " + isMaximized); }
            @Override
            public void created(Lwjgl3Window window) { App.log(1, "created : " + window); }
            @Override
            public void refreshRequested() { App.log(1, "refreshRequested"); }

        };
    }
}

/**
 * {@summary Provide special for Desktop function.}
 */
class DesktopNative implements Native {
    @Override
    public void toFront() {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        window.focusWindow();
    }
    @Override
    public void exit(int code) { System.exit(code); }
}

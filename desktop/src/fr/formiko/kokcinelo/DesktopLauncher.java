package fr.formiko.kokcinelo;

import fr.formiko.kokcinelo.tools.OptionsMap;
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
        OptionsMap options = Controller.loadOptions();
        App.log(1, "options : " + options);
        int displayMode = options.getInt("displayMode");
        int maxFps = options.getInt("maxFps");

        if (Os.getOs().isLinux()) { // TODO to remove
            displayMode = 0;
        }

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(maxFps); // 0 = no limite
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
                int screenWidth = options.getInt("screenWidth");
                int screenHeigth = options.getInt("screenHeigth");
                if (screenWidth > 0 && screenHeigth > 0) {
                    App.log(1, "Start on windowed mode : " + screenWidth + "x" + screenHeigth + ".");
                    config.setWindowedMode(screenWidth, screenHeigth);
                    break;
                } else {
                    config.setMaximized(true);
                }
        }
        // config.setResizable(false);
        config.useVsync(true);
        config.setTitle("Kokcinelo");
        config.setWindowIcon("images/icons/appIcon.png");
        App game = new App(args, new DesktopNative());
        new Lwjgl3Application(game, config);
    }
}


class DesktopNative implements Native {
    @Override
    public void toFront() {
        Lwjgl3Window window = ((Lwjgl3Graphics) Gdx.graphics).getWindow();
        window.focusWindow();
    }
}

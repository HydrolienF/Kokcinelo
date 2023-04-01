package fr.formiko.kokcinelo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
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
	private static int displayMode = 2;

	public static void main(String[] args) {
		if (args.length > 0 && args[0].replaceAll("-", "").equalsIgnoreCase("version")) {
			try {
				InputStream is = new DesktopLauncher().getClass().getClassLoader().getResourceAsStream("version.md");
				String version = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
						.collect(Collectors.joining("\n"));
				System.out.println(version);
				System.exit(0);
			} catch (Exception e) {
				System.out.println("Fail to get version in DesktopLauncher.");
			}
		}
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		switch (displayMode) {
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
			config.setMaximized(true);
			break;
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
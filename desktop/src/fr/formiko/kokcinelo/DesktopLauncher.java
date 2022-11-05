package fr.formiko.kokcinelo;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
/**
 * {@summary Launch for Windows, MacOs &#38; linux.}
 * 
 * @author Hydrolien
 * @version 0.1
 * @since 0.1
 */
public class DesktopLauncher {
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(1920, 1080);
		// config.useVsync(true);
		config.setTitle("Kokcinelo");
		new Lwjgl3Application(new App(args), config);
	}
}

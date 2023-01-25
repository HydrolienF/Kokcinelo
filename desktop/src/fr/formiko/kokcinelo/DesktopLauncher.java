package fr.formiko.kokcinelo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
/**
 * {@summary Launch for Windows, MacOs &#38; linux.}
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.1
 */
public class DesktopLauncher {
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
		config.setMaximized(true);
		config.setResizable(false);
		config.useVsync(true);
		config.setTitle("Kokcinelo");
		config.setWindowIcon("images/icons/appIcon.png");
		App game = new App(args);
		// game.setLanguage(System.getProperty("user.language"));
		new Lwjgl3Application(game, config);
	}
}

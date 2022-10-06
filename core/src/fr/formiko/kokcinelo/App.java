package fr.formiko.kokcinelo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class App extends Game {
	SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		GameScreen gs = new GameScreen(this);
		this.setScreen(gs);
		// this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		ScreenUtils.clear(1, 0, 0, 1);
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		// try {
		// 	batch.end();
		// 	batch.dispose();
		// } catch (Exception e) {
		// 	System.out.println(e);
		// }
	}
}

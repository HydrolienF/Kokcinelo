package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.tools.Shapes;
import fr.formiko.usual.Chrono;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class EnvironmentMenuScreen extends Table {
    private MenuScreen menuScreen;
    private final Chrono chrono;
    private float skyPercent = 0.5f;

    public EnvironmentMenuScreen(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
        // this.setFillParent(true);
        chrono = new Chrono();
        chrono.start();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // switch (menuScreen.getLevel().getLetter()) {
        // case "K":
        // // draw blue sky gradient
        // float secToCycle = 3 * 60 + 54;
        // // float secToCycle = 10;
        // chrono.updateDuree();
        // float ligth = 1 - ((chrono.getDuree() % (secToCycle * 1000) / (secToCycle * 1000f))); // [0.5 ; 1]
        // if (ligth < 0.5f) {
        // ligth = 1f - ligth;
        // }
        // Shapes.drawSky(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), ligth);
        // break;
        // case "F":
        // Shapes.drawUnderground(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0.6f, 0.4f);
        // break;
        // }


        float secToCycle = 3 * 60 + 54;
        chrono.updateDuree();
        float ligth = 1 - ((chrono.getDuree() % (secToCycle * 1000) / (secToCycle * 1000f))); // [0.5 ; 1]
        if (ligth < 0.5f) {
            ligth = 1f - ligth;
        }
        Shapes.drawSky(0, Gdx.graphics.getHeight() * (1 - skyPercent), Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * skyPercent,
                ligth);
        Shapes.drawUnderground(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * (1 - skyPercent), 0.6f, 0.4f);
    }
}

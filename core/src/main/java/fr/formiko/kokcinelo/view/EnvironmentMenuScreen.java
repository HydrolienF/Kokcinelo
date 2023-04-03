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
    private boolean withGrass;

    public EnvironmentMenuScreen(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
        // this.setFillParent(true);
        chrono = new Chrono();
        chrono.start();
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // setZoom(2f);
    }

    public void setZoom(float zoom) { setScale(zoom, zoom); }
    public boolean isWithGrass() { return withGrass; }
    public void setWithGrass(boolean withGrass) { this.withGrass = withGrass; }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setWithGrass(!menuScreen.getLevel().getLetter().equals("F"));
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
        Shapes.drawSky(getX(), getY() + getHeight() * (1 - skyPercent), getWidth(), getHeight() * skyPercent, ligth);
        if (withGrass) {
            Shapes.drawGrass(getX(), getY(), getWidth(), getHeight() * (1 - skyPercent), ligth);
        } else {
            Shapes.drawUnderground(getX(), getY(), getWidth(), getHeight() * (1 - skyPercent), 0.6f, 0.4f);
        }
    }

}

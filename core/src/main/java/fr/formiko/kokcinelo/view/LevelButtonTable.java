package fr.formiko.kokcinelo.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LevelButtonTable extends Table {
    private final ShapeRenderer sr;
    private final int lineWidth;

    public LevelButtonTable(int lineWidth) {
        super();
        sr = new ShapeRenderer();
        this.lineWidth = lineWidth;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sr.setColor(Color.BLACK);
        batch.end();
        sr.begin(ShapeType.Filled);
        for (LevelButton levelButton : LevelButton.getList()) {
            int x1 = (int) levelButton.getCenterX();
            int y1 = (int) levelButton.getCenterY();
            LevelButton levelButton2 = LevelButton.getLevelButton((levelButton.getNumber() - 1) + levelButton.getLetter());
            if (levelButton2 != null) {
                int x2 = (int) levelButton2.getCenterX();
                int y2 = (int) levelButton2.getCenterY();
                sr.rectLine(x1, y1, x2, y2, lineWidth);
            }
            if (levelButton.getNumber() == 1) {
                levelButton2 = LevelButton.getLevelButton((levelButton.getNumber() + 1) + "F");
                int x2 = (int) levelButton2.getCenterX();
                int y2 = (int) levelButton2.getCenterY();
                sr.rectLine(x1, y1, x2, y2, lineWidth);
            }
        }
        sr.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }
}

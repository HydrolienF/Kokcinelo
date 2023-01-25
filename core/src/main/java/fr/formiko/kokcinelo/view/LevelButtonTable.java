package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.model.Level;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * {@summary Table containing button to select a level in main menu.}
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
public class LevelButtonTable extends Table {
    private ShapeDrawer sr;
    private final int lineWidth;

    /**
     * {@summary Main constructor.}
     * 
     * @param lineWidth width of the line between buttons
     */
    public LevelButtonTable(int lineWidth) {
        super();
        this.lineWidth = lineWidth;
    }

    /**
     * {@summary Draw the lines between buttons &#38; buttons.}
     * 
     * @param batch       batch to draw on
     * @param parentAlpha alpha of the parent
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (sr == null) {
            Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
            sr = new ShapeDrawer(batch, region);
            texture.dispose();
        }
        sr.setColor(Color.BLACK);
        for (LevelButton levelButton : LevelButton.getList()) {
            int x1 = (int) levelButton.getCenterX();
            int y1 = (int) levelButton.getCenterY();
            for (Level next : levelButton.getLevel().getNextLevels()) {
                LevelButton levelButton2 = LevelButton.getLevelButton(next.getId());
                if (levelButton2 != null) {
                    int x2 = (int) levelButton2.getCenterX();
                    int y2 = (int) levelButton2.getCenterY();
                    sr.line(x1, y1, x2, y2, lineWidth);
                }
            }
        }
        super.draw(batch, parentAlpha);
    }
}

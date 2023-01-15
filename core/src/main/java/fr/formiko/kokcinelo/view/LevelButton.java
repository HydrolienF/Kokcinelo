package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.tools.Shapes;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

class LevelButton extends Button {
    private static Set<LevelButton> levelButtonList;
    private static Texture circle;
    private static Texture circleSelected;
    private final String id;


    public LevelButton(int radius, Skin skin, String id) {
        super(skin);
        this.id = id;
        setSize(radius * 2, radius * 2);

        // Initialised by lazy
        if (circle == null) {
            circle = Shapes.getCircle(radius, radius / 10, Color.BLACK);
        }
        if (circleSelected == null) {
            circleSelected = Shapes.getCircle(radius, radius / 5, Color.ORANGE);
        }
        if (levelButtonList == null) {
            levelButtonList = new HashSet<LevelButton>();
        }

        levelButtonList.add(this);
        addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) { setChecked(true); }
        });
    }


    public String getId() { return id; }
    /**
     * Setter that make sure that only one button is checked a same time.
     */
    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            for (LevelButton lb : levelButtonList) {
                if (lb.isChecked()) {
                    App.log(0, "Unchecked (by check)" + lb.getId());
                    lb.setChecked(false);
                }
            }
            App.log(0, "Check " + getId());
        } else {
            App.log(0, "Unchecked " + getId());
        }
        super.setChecked(checked);
    }
    /**
     * @return the only checked button
     */
    public static LevelButton getCheckedButton() {
        for (LevelButton levelButton : levelButtonList) {
            if (levelButton.isChecked()) {
                return levelButton;
            }
        }
        return null;
    }
    public static void clearList() { levelButtonList = null; }
    public static Set<LevelButton> getList() { return levelButtonList; }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        // Gdx.gl.glColorMask(true, true, true, true);
        // Gdx.gl.glDepthFunc(GL30.GL_LESS);
        if (isChecked() || isOver()) {
            batch.draw(circleSelected, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(circle, getX(), getY(), getWidth(), getHeight());
        }
    }
}
package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.tools.Shapes;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

class LevelButton extends Button {
    private static Texture circle;
    private static Texture circleSelected;
    private boolean selected;
    private final String id;


    public LevelButton(int radius, Skin skin, String id) {
        super(skin);
        this.id = id;
        setSize(radius * 2, radius * 2);
        // TODO if isSelected circle is orrange
        if (circle == null) {
            circle = Shapes.getCircle(radius, radius / 10, Color.BLACK);
        }
        if (circleSelected == null) {
            circleSelected = Shapes.getCircle(radius, radius / 5, Color.ORANGE);
        }
    }


    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public String getId() { return id; }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        // Gdx.gl.glColorMask(true, true, true, true);
        // Gdx.gl.glDepthFunc(GL30.GL_LESS);
        if (selected) {
            batch.draw(circleSelected, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(circle, getX(), getY(), getWidth(), getHeight());
        }
    }
}
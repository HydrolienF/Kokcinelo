package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.tools.Shapes;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * {@summary Button to select a level in main menu.}
 * 
 * @author Hydrolien
 * @version 0.2
 * @since 0.2
 */
class LevelButton extends Button {
    private static Set<LevelButton> levelButtonList;
    private static Texture circle;
    private static Texture circleSelected;
    private static Texture circleSelectedDisable;
    private static Texture lockedLevel;
    private Texture texture;
    private final String id;
    private static Set<String> unlockedLevels;

    /**
     * {@summary Main constructor.}
     * It add listener to react to user cick depending of the state of the button.
     * 
     * @param radius radius of the button
     * @param skin   graphic style to use
     * @param id     id of the button
     * @param ms     link to the menu screen
     */
    public LevelButton(int radius, Skin skin, String id, MenuScreen ms) {
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
        if (circleSelectedDisable == null) {
            circleSelectedDisable = Shapes.getCircle(radius, radius / 5, Color.GRAY);
        }
        if (levelButtonList == null) {
            levelButtonList = new HashSet<LevelButton>();
        }
        if (lockedLevel == null) {
            lockedLevel = Shapes.getCircledTexture(radius, new Color(0.3f, 0.3f, 0.3f, 1f),
                    new Texture(Gdx.files.internal("images/lock.png")), 0.3f);
        }

        levelButtonList.add(this);
        addListener(new ClickListener() {
            /**
             * React to user click by set selected to true if button can be selected.
             */
            public void clicked(InputEvent event, float x, float y) {
                if (!isDisabled()) {
                    ms.updateSelectedLevel(getId());
                    setChecked(true);
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) { ms.updateOveredLevel(getId()); }
            /**
             * Reset overed level if mouse exit the button.
             */
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    ms.updateOveredLevel(getCheckedButton().getId());
                }
            }
        });

        setDisabled(!isUnlocked() || !App.isPlayableLevel(id));

        // TODO make button display 0 to 3 starts at 50%, 80%, 100% of the level
    }

    // GET SET -----------------------------------------------------------------
    public String getId() { return id; }
    public int getNumber() { return Integer.parseInt(id.substring(0, 1)); }
    public String getLetter() { return id.substring(1, 2); }
    public float getCenterX() { return getX() + getWidth() / 2; }
    public float getCenterY() { return getY() + getHeight() / 2; }
    public void setCenterX(float x) { setX(x - getWidth() / 2); }
    public void setCenterY(float y) { setY(y - getHeight() / 2); }
    /**
     * Setter that make sure that only one button is checked a same time.
     */
    @Override
    public void setChecked(boolean checked) {
        if (isDisabled()) {
            return;
        } // if button is disable, don't do anything.
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
    /**
     * @return the only overed button or the only checked button if no button is overed.
     */
    public static LevelButton getOveredButton() {
        for (LevelButton levelButton : levelButtonList) {
            if (levelButton.isOver()) {
                return levelButton;
            }
        }
        return getCheckedButton();
    }
    public static Set<LevelButton> getList() { return levelButtonList; }
    /**
     * @param id id of the level button to get.
     * @return a level button by it's id.
     */
    public static LevelButton getLevelButton(String id) {
        for (LevelButton levelButton : levelButtonList) {
            if (levelButton.getId().equals(id)) {
                return levelButton;
            }
        }
        return null;
    }
    /**
     * @return true if the level is unlocked.
     */
    public boolean isUnlocked() {
        if (unlockedLevels == null) {
            unlockedLevels = Controller.getController().loadUnlockedLevels();
        }
        return unlockedLevels.contains(id);
    }

    // FUNCTIONS ---------------------------------------------------------------
    public static void clearList() { levelButtonList = null; }

    /**
     * {@summary Draw the button with the rigth border circle &#38; the rigth content texture.}
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        // Gdx.gl.glColorMask(true, true, true, true);
        // Gdx.gl.glDepthFunc(GL30.GL_LESS);
        if (isDisabled()) {
            batch.draw(lockedLevel, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
        }

        if (isChecked() || isOver()) {
            if (isDisabled()) {
                batch.draw(circleSelectedDisable, getX(), getY(), getWidth(), getHeight());
            } else {
                batch.draw(circleSelected, getX(), getY(), getWidth(), getHeight());
            }
        } else {
            batch.draw(circle, getX(), getY(), getWidth(), getHeight());
        }
    }
    /**
     * @return the texture of the level button depending of the level id.
     */
    private Texture getTexture() {
        if (texture == null) {
            // TODO return a texture depending of the level id.
            switch (id) {
            case "1K": {
                texture = Shapes.getCircledTexture((int) (getWidth() / 2), App.GREEN, new Texture(Gdx.files.internal("images/aphid.png")),
                        0.6f);
                break;
            }
            default: {
                texture = Shapes.getCircle((int) (getWidth() / 2), App.GREEN);
            }
            }

        }
        return texture;
    }
}
package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Ant;
import fr.formiko.kokcinelo.model.Aphid;
import fr.formiko.kokcinelo.model.BigScoreAphid;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.GreenAnt;
import fr.formiko.kokcinelo.model.HealthAphid;
import fr.formiko.kokcinelo.model.Ladybug;
import fr.formiko.kokcinelo.model.Level;
import fr.formiko.kokcinelo.model.RedAnt;
import fr.formiko.kokcinelo.model.ScoreAphid;
import fr.formiko.kokcinelo.model.SpeedAphid;
import fr.formiko.kokcinelo.tools.Shapes;
import java.util.HashSet;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private static Texture star;
    private static boolean needToRefreshUnlockedLevel;
    private Sprite sprite;
    private final Level level;
    private final int radius;
    private final int bestScore;

    /**
     * {@summary Main constructor.}
     * It add listener to react to user cick depending of the state of the button.
     * 
     * @param radius radius of the button
     * @param skin   graphic style to use
     * @param id     id of the level of the button
     * @param ms     link to the menu screen
     */
    public LevelButton(int radius, Skin skin, String id, MenuScreen ms) {
        super(skin);
        level = Level.getLevel(id);
        this.radius = radius;
        // int size = (int) (radius * 2.5f);
        int size = (int) (radius * 2f);
        setSize(size, size);

        // Initialised by lazy
        if (circle == null) {
            circle = Shapes.getCircle(radius, radius / 10, Color.BLACK);
        }
        if (circleSelected == null) {
            circleSelected = Shapes.getCircle(radius, radius / 5, new Color(0, 0, 0.75f, 1));
        }
        if (circleSelectedDisable == null) {
            circleSelectedDisable = Shapes.getCircle(radius, radius / 5, Color.GRAY);
        }
        if (levelButtonList == null) {
            levelButtonList = new HashSet<>();
        }
        if (lockedLevel == null) {
            lockedLevel = Shapes.getCircledTexture(radius, new Color(0.3f, 0.3f, 0.3f, 1f),
                    new Texture(Gdx.files.internal("images/lock.png")), 0.3f);
        }
        if (star == null) {
            int starSize = ((int) (radius * 0.6f));
            star = Shapes.outLine(Shapes.resize(new Texture(Gdx.files.internal("images/star.png")), starSize, starSize));
        }

        levelButtonList.add(this);
        addListener(new ClickListener() {
            /**
             * React to user click by set selected to true if button can be selected.
             */
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isDisabled()) {
                    ms.updateSelectedLevel(getId());
                    setChecked(true);
                    App.playSound("clicOff");
                }
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                ms.updateOveredLevel(getId());
                if (pointer == -1) {
                    App.playSound("clicOn");
                }
            }
            /**
             * Reset overed level if mouse exit the button.
             */
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    ms.updateOveredLevel(getCheckedButton().getId());
                    // App.playSound("clic");
                }
            }
        });

        setDisabled(!isUnlocked() || !App.isPlayableLevel(id));

        if (!isDisabled()) {
            bestScore = Controller.getController().getBestScore(getId());
        } else {
            bestScore = 0;
        }
    }

    // GET SET -----------------------------------------------------------------
    public Level getLevel() { return level; }
    public String getId() { return getLevel().getId(); }
    public int getNumber() { return getLevel().getNumber(); }
    public String getLetter() { return getLevel().getLetter(); }
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
        // set "1K" to checked if no button is checked.
        LevelButton lb = getLevelButton("1K");
        lb.setChecked(true);
        return lb;
    }
    /**
     * @return the only overed button or the only checked button if no button is overed.
     */
    public static LevelButton getOveredButton() {
        return levelButtonList.stream().filter(lb -> lb.isOver()).findFirst().orElse(getCheckedButton());
    }
    public static Set<LevelButton> getList() { return levelButtonList; }
    /**
     * @param id id of the level button to get.
     * @return a level button by it's id.
     */
    public static LevelButton getLevelButton(String id) {
        LevelButton lbr = levelButtonList.stream().filter(lb -> lb.getId().equals(id)).findFirst().orElse(null);
        if (lbr == null) {
            App.log(3, "LevelButton not found: " + id);
            throw new RuntimeException("LevelButton not found: " + id);
        }
        return lbr;
    }
    /**
     * @return true if the level is unlocked.
     */
    public boolean isUnlocked() {
        if (needToRefreshUnlockedLevel) {
            Controller.loadUnlockedLevels();
            needToRefreshUnlockedLevel = false;
        }
        return level.isUnlocked();
    }
    public static void resetUnlockedLevels() { needToRefreshUnlockedLevel = true; }

    // FUNCTIONS ---------------------------------------------------------------
    public static void clearList() { levelButtonList = null; }

    /**
     * {@summary Draw the button with the rigth border circle &#38; the rigth content sprite.}
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        // Gdx.gl.glColorMask(true, true, true, true);
        // Gdx.gl.glDepthFunc(GL30.GL_LESS);
        if (isDisabled()) {
            batch.draw(lockedLevel, getX(), getY());
        } else {
            // batch.draw(getTexture(), getX(), getY());
            sprite = getSprite();
            if (sprite != null) {
                sprite.setPosition(getX(), getY());
                sprite.draw(batch);
            }
        }

        if (isChecked() || isOver()) {
            if (isDisabled()) {
                batch.draw(circleSelectedDisable, getX(), getY(), radius * 2f, radius * 2f);
            } else {
                batch.draw(circleSelected, getX(), getY(), radius * 2f, radius * 2f);
            }
        } else {
            batch.draw(circle, getX(), getY(), radius * 2f, radius * 2f);
        }
        if (!isDisabled()) {
            if (bestScore >= App.STARS_SCORES.get(0)) {
                batch.draw(star, getX(), getY());
            }
            if (bestScore >= App.STARS_SCORES.get(1)) {
                batch.draw(star, getX() + (getWidth() - star.getWidth()) / 2, getY() - star.getWidth() / 3f);
            }
            if (bestScore >= App.STARS_SCORES.get(2)) {
                batch.draw(star, getX() + getWidth() - star.getWidth(), getY());
            }
        }
    }
    /**
     * @return the texture of the level button depending of the level id.
     */
    private Sprite getSprite() {
        Creature c, c2, c3, c4;
        final float aphidSize = 0.07f;
        final float smallAphidSize = 0.04f;
        final float antSize = 0.075f;
        final float ladybugSize = 0.33f;
        if (sprite == null) {
            switch (getId()) {
                case "1K": {
                    c = new Aphid();
                    c.setZoom(aphidSize);
                    c.setCollectedFrequency(-1); // to make sure it's not with honeydew
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c);
                    break;
                }
                case "2K": {
                    c = new Ladybug();
                    c.setZoom(ladybugSize);
                    c2 = new RedAnt();
                    c2.setZoom(antSize);
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c, c2);
                    break;
                }
                case "3K": {
                    c = new Ladybug();
                    c.setZoom(ladybugSize);
                    c2 = new GreenAnt();
                    c2.setZoom(antSize);
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c, c2);
                    break;
                }
                case "4K": {
                    c = new Ladybug();
                    c.setZoom(ladybugSize);
                    c2 = new SpeedAphid();
                    c2.setZoom(smallAphidSize);
                    c3 = new HealthAphid();
                    c3.setZoom(smallAphidSize);
                    c4 = new ScoreAphid();
                    c4.setZoom(smallAphidSize);
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c, c2, c3, c4);
                    break;
                }
                case "2F": {
                    c = new Ladybug();
                    c.setZoom(ladybugSize);
                    c2 = new RedAnt();
                    c2.setZoom(antSize);
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c2, c);
                    break;
                }
                case "3F": {
                    c = new Ladybug();
                    c.setZoom(ladybugSize);
                    c2 = new GreenAnt();
                    c2.setZoom(antSize);
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c2, c);
                    break;
                }
                case "4F": {
                    c = new Ant();
                    c.setZoom(antSize);
                    c2 = new SpeedAphid();
                    c2.setZoom(smallAphidSize);
                    c3 = new HealthAphid();
                    c3.setZoom(smallAphidSize);
                    c4 = new ScoreAphid();
                    c4.setZoom(smallAphidSize);
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c, c2, c3, c4);
                    break;
                }
                case "4A": {
                    c = new BigScoreAphid();
                    c.setZoom(aphidSize);
                    c2 = new Ladybug();
                    c2.setZoom(ladybugSize);
                    sprite = Shapes.getCircledSprite(radius, App.GREEN, c, c2);
                    break;
                }
                default: {
                    sprite = new Sprite(Shapes.getCircle(radius, App.GREEN));
                }
            }

        }
        return sprite;
    }
}

package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.tools.KProgressBar;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;

/**
 * {@summary A custom progress bar for creature action.}
 * The only changes are the addition of a label to display the action name and the change of the background color when the action is ready.
 * Color change wen progress bar is full allow to see clearly when the action is ready.
 * 
 * @author Hydrolien
 * @version 2.5
 * @since 2.5
 */
public class CreatureActionProgressBar extends KProgressBar {
    protected @Null Color actionReadyColor;
    protected @Null Color actionNotReadyColor;
    protected final Label actionNameLabel;

    /**
     * {@summary Main constructor.}
     * 
     * @param width                Width of the progress bar in pixel.
     * @param height               Height of the progress bar in pixel. If -1, height will be 1/10 of width.
     * @param backgroundFullColor  Color of the full part of the progress bar when action is not ready.
     * @param backgroundEmptyColor Color of the empty part of the progress bar.
     * @param borderColor          Color of the border of the progress bar.
     * @param actionReadyColor     Color of the full part of the progress bar when action is ready.
     * @param actionName           Name of the action to display.
     * @param skin                 Skin to use for the label.
     */
    public CreatureActionProgressBar(int width, int height, @Null Color backgroundFullColor, @Null Color backgroundEmptyColor,
            @Null Color borderColor, @Null Color actionReadyColor, String actionName, Skin skin) {
        super(width, height, backgroundFullColor, backgroundEmptyColor, borderColor);
        this.actionReadyColor = actionReadyColor;
        this.actionNotReadyColor = backgroundFullColor;
        actionNameLabel = new Label(actionName, skin);
        // Refresh the label location
        actionNameLabel.setAlignment(Align.center);
        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * {@summary Set progress &#38; change background color if progress is 1.}
     */
    @Override
    public void setProgress(float progress) {
        super.setProgress(progress);
        if (progress > 0.9999f) {
            backgroundFullColor = actionReadyColor;
        } else {
            backgroundFullColor = actionNotReadyColor;
        }
    }
    /**
     * {@summary Draw the progress bar and the action name label.}
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (actionNameLabel != null) {
            actionNameLabel.draw(batch, parentAlpha);
        }
    }

    /**
     * {@summary Setter that also apply to actionNameLabel.}
     */
    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (actionNameLabel != null) {
            actionNameLabel.setSize(width, height);
        }
    }
    /**
     * {@summary Setter that also apply to actionNameLabel.}
     */
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (actionNameLabel != null) {
            actionNameLabel.setPosition(x, y);
        }
    }
    /**
     * {@summary Setter that also apply to actionNameLabel.}
     */
    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        if (actionNameLabel != null) {
            actionNameLabel.setBounds(x, y, width, height);
        }
    }
}

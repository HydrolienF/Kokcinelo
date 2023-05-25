package fr.formiko.kokcinelo.tools;

import fr.formiko.kokcinelo.App;
import java.util.function.IntConsumer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * A single line int input field.
 * 
 * @author Hydrolien
 * @version 2.4
 * @since 2.4
 */
public class IntTextField extends TextField {
    private int prefWidth;

    /**
     * {@summary Create a new IntTextField.}
     * 
     * @param text             The text to display.
     * @param skin             The skin to use.
     * @param prefWidth        The preferred width.
     * @param alignment        The alignment. See {@link com.badlogic.gdx.utils.Align}.
     * @param onChangeFunction The function to call when the text change.
     */
    public IntTextField(int text, Skin skin, int prefWidth, int alignment, IntConsumer onChangeFunction) {
        super(String.valueOf(text), skin);
        this.prefWidth = prefWidth;
        setAlignment(alignment);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { App.playSound("clicOn"); }
        });
        setTextFieldListener((textField, c) -> {
            App.log(0, "IntTextField: " + textField.getText());
            try {
                onChangeFunction.accept(Integer.parseInt(textField.getText()));
            } catch (NumberFormatException nfe) {
                App.log(2, "Bad format for number in textfield" + textField.getText());
            }
        });
        setTextFieldFilter((textField, c) -> ((c >= '0' && c <= '9') || c == '-' || c == '+'));
    }

    @Override
    public float getPrefWidth() { return prefWidth; }
}

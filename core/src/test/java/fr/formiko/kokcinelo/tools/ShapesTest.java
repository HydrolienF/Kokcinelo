package fr.formiko.kokcinelo.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ShapesTest extends Assertions {
    @ParameterizedTest
    @CsvSource({
        "10, 10, 1",
    })
    void testDrawGradientOnPixmap(int width, int height, int times){
        // TODO, fail to create new pixmap for now.
        for(int i=0; i<times; i++){
            // Pixmap pm = new Pixmap(width, height, Format.RGBA8888);
            // Shapes.drawGradientOnPixmap(pm, Color.BLUE, Color.ORANGE);
            // assertEquals(0, pm.getPixel(0, 0));
        }
    }
    
}

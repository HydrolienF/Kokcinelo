package fr.formiko.kokcinelo.model;

import fr.formiko.kokcinelo.Controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ScoreAphidTest extends Assertions {
    @Test
    public void testGetGivenPoints() { assertTrue(new ScoreAphid().getGivenPoints() > new Aphid().getGivenPoints()); }
    @ParameterizedTest
    @CsvSource({"100, 1, 0, 50, 52, ", "100, 1, 1, 50, 48"})
    public void testBonusWhenCollectHoneydew(int aphid, int ladybug, int ant, int defaultScore, int scoreExepted) {
        CreatureTest.createGameStateWithAphidLadybugAnt(aphid, ladybug, ant, "1K");
        ScoreAphid a = new ScoreAphid();
        Creature pc = ant == 0 ? Controller.getController().getGameState().getLadybugs().get(0)
                : Controller.getController().getGameState().getAnts().get(0);
        Controller.getController().getGameState().getAphids().add(a);
        Controller.getController().getLocalPlayer().setScore(defaultScore);
        a.bonusWhenCollectHoneydew(pc);
        assertEquals(scoreExepted, Controller.getController().getLocalPlayer().getScore());
    }
}

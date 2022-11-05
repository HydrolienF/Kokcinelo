package fr.formiko.kokcinelo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AppTest extends Assertions {
    @Test
    public void testAdd() { assertEquals(43, Integer.sum(20, 23)); }
}


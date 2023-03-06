package my.ylab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PellTest {

    @Test
    void getPellNumber() {
        Pell pell = new Pell();
        int actual = pell.getPellNumber(5);
        int expected = 29;
        assertEquals(29, actual);
    }
}
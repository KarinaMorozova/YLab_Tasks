package io.ylab.intensive.lesson01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PellTest {
    @Test
    void getPellNumber() {
        Pell pell = new Pell();
        int actual = pell.getPellNumber(5);
        int expected = 29;
        assertEquals(expected, actual);


    }
}
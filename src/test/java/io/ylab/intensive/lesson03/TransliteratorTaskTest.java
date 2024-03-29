package io.ylab.intensive.lesson03;

import io.ylab.intensive.lesson03.transliterator.Transliterator;
import io.ylab.intensive.lesson03.transliterator.TransliteratorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransliteratorTaskTest {

    @Test
    public void transliterationCheck() {
        Transliterator transliterator = new TransliteratorImpl();
        String actual = transliterator
                .transliterate("ЛЮЛЮКОВ");
        assertEquals(actual, "LIULIUKOV");

        actual = transliterator
                .transliterate("ЛЯПКИН-ТЯПКИН");
        assertEquals(actual, "LIAPKIN-TIAPKIN");

        actual = transliterator
                .transliterate("ЯИЧНИЦА");
        assertEquals(actual, "IAICHNITSA");

        actual = transliterator
                .transliterate("СИМЕОНОВ-ПИЩИК");
        assertEquals(actual, "SIMEONOV-PISHCHIK");
    }
}

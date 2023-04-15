package io.ylab.intensive.lesson02.validator;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SnilsValidatorTaskTest {

    @Test
    void check() {
        boolean actual = new SnilsValidatorImpl().validate("");
        assertFalse(actual);

        actual = new SnilsValidatorImpl().validate("45-6--78-32");
        assertFalse(actual);

        actual = new SnilsValidatorImpl().validate(null);
        assertFalse(actual);

        actual = new SnilsValidatorImpl().validate("%^WE(JDNV%(^&()$");
        assertFalse(actual);

        // строка с верным СНИЛС с незначащими пробелами по бокам пройдет валидацию
        actual = new SnilsValidatorImpl().validate(" 90114404441 ");
        assertTrue(actual);

        actual = new SnilsValidatorImpl().validate("90114404441");
        assertTrue(actual);
    }
}

package my.ylab.homework02.complex;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexNumberTaskTest {

    @Test
    void getSum() {
        ComplexNumber first = new ComplexNumber(2, 1);
        ComplexNumber second = new ComplexNumber(3, 4);

        ComplexNumber actual = first.add(second);
        ComplexNumber expected = new ComplexNumber(5, 5);

        assertEquals(expected, actual);
    }

    @Test
    void getDifference() {
        ComplexNumber first = new ComplexNumber(2, 5);
        ComplexNumber second = new ComplexNumber(1, 4);

        ComplexNumber actual = first.subtract(second);
        ComplexNumber expected = new ComplexNumber(1, 1);

        assertEquals(expected, actual);
    }

    @Test
    void getProduct() {
        // (a + ib) * (c + id) = (ac – bd) + i(ad + bc)
        ComplexNumber first = new ComplexNumber(2, 1);
        ComplexNumber second = new ComplexNumber(3, 4);

        ComplexNumber actual = first.multiply(second);
        ComplexNumber expected = new ComplexNumber(2, 11);

        assertEquals(expected, actual);
    }

    @Test
    void getModulus() {
        // |a + ib| = Math.sqrt(a * a + b * b)
        ComplexNumber first = new ComplexNumber(4, 3);

        double actual = first.modulus();
        double expected = 5;
        assertEquals(expected, actual);
    }

    @Test
    void getStringRepresentation() {
        // |a + ib| = a * a + b * b
        ComplexNumber first = new ComplexNumber(3.2, 2.3);

        String actual = first.toString();
        String expected = "3.2 + 2.3i";
        assertEquals(expected, actual);

        ComplexNumber second = new ComplexNumber(3, 2);

        actual = second.toString();
        expected = "3 + 2i";
        assertEquals(expected, actual);

        ComplexNumber third = new ComplexNumber(0, 2);

        actual = third.toString();
        expected = "2i";
        assertEquals(expected, actual);

        ComplexNumber forth = new ComplexNumber(2, 0);

        actual = forth.toString();
        expected = "2";
        assertEquals(expected, actual);

        ComplexNumber fifth = new ComplexNumber(0.0, 0.0);

        actual = fifth.toString();
        expected = "0";
        assertEquals(expected, actual);
    }
}

package io.ylab.intensive.lesson02.complex;

public class ComplexNumbersTest {
    public static void main(String[] args) {
        ComplexNumber first = new ComplexNumber(4, 3);

        System.out.println("Первое число a = " + first);

        ComplexNumber second = new ComplexNumber(2, 2);

        System.out.println("Второе число b = " + second);

        System.out.println("Сумма чисел a + b = " + first.add(second));

        System.out.println("Разность чисел a - b = " + first.subtract(second));

        System.out.println("Произведение чисел a * b = " + first.multiply(second));

        System.out.println("Модуль числа a = " + first.modulus());
    }
}

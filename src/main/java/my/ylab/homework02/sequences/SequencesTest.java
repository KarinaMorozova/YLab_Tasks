package my.ylab.homework02.sequences;

import java.util.Scanner;

public class SequencesTest {
    public static void main(String[] args) {
        Sequences sequences = new SequencesImpl();
        System.out.println("Для вывода последовательностей введите число целое N в интервале [1, 15]");

        try (Scanner scanner = new Scanner(System.in)) {
            int n;

            try {
                n = scanner.nextInt();

                if (n > 0 && n <= 15) {
                    sequences.a(n);
                    sequences.b(n);
                    sequences.c(n);
                    sequences.d(n);
                    sequences.e(n);
                    sequences.f(n);
                    sequences.g(n);
                    sequences.h(n);
                    sequences.i(n);
                    sequences.j(n);
                }
                else {
                    throw new Exception();
                }
            } catch (Exception ex) {
                System.out.println("Неверный формат числа");
            }
        }
    }
}

package io.ylab.intensive.lesson01;

import java.util.Scanner;

public class Pell {
    public int getPellNumber(int n) {
        int[] pell = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            if (i > 1) {
                pell[i] = 2 * pell[i - 1] + pell[i - 2];
            } else {
                pell[i] = i;
            }
        }

        return pell[n];
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите целое число от 0 до 30 включительно");

            int n = scanner.nextInt();
            System.out.println(new Pell().getPellNumber(n));
        }
    }
}
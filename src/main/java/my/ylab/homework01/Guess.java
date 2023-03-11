package my.ylab.homework01;

import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) {
        int number = new Random().nextInt(99) + 1; // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");

        boolean isGuessed = false;
        try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 1; i <= maxAttempts ; i++) {
                int attempt = scanner.nextInt();
                if (attempt == number) {
                    System.out.println("Ты угадал с " + i + " попытки");
                    isGuessed = true;
                } else if (attempt > number) {
                    System.out.println("Мое число меньше! У тебя осталось " + (maxAttempts - i) + " попыток");
                } else {
                    System.out.println("Мое число больше! У тебя осталось " + (maxAttempts - i) + " попыток");
                }
            }
            if (!isGuessed) {
                System.out.println("Ты не угадал");
            }
        }

    }
}

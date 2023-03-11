package my.ylab.homework01;

import java.util.Scanner;

public class Stars {
    public int n;
    public int m;
    public String c;

    public Stars(int n, int m, String c) {
        this.n = n;
        this.m = m;
        this.c = c;
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String c = scanner.next();

            Stars stars = new Stars(n, m, c);
            stars.print();
        }
    }
}

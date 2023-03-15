package my.ylab.homework02.sequences;

import java.util.Arrays;

public class SequencesImpl implements Sequences {
    public void print(int[] array) {
        Arrays.stream((array)).forEach(System.out::println);
        System.out.println();
    }

    @Override
    public void a(int n) {
        // A: 2, 4, 6, 8, 10...
        int[] result = new int[n];

        for (int k = 0; k < n; k++) {
            result[k] = 2 + 2 * k;
        }

        print(result);
    }

    @Override
    public void b(int n) {
        // B: 1, 3, 5, 7, 9...
        int[] result = new int[n];

        for (int k = 0; k < n; k++) {
            result[k] = 1 + 2 * k;
        }

        print(result);
    }

    @Override
    public void c(int n) {
        // C: 1, 4, 9, 16, 25...
        int[] result = new int[n];

        for (int k = 1; k < n + 1; k++) {
            result[k - 1] = k * k;
        }

        print(result);
    }

    @Override
    public void d(int n) {
        // D: 1, 8, 27, 64, 125...
        int[] result = new int[n];

        for (int k = 1; k < n + 1; k++) {
            result[k - 1] = k * k * k;
        }

        print(result);
    }

    @Override
    public void e(int n) {
        // E: 1, -1, 1, -1, 1, -1...
        int[] result = new int[n];

        for (int k = 0; k < n; k++) {
            result[k] = (k % 2 == 0) ? 1 : -1;
        }

        print(result);
    }

    @Override
    public void f(int n) {
        // F: 1, -2, 3, -4, 5, -6...
        int[] result = new int[n];

        for (int k = 1; k < n + 1; k++) {
            result[k - 1] = (k % 2 == 0) ? (-1) * k : k;
        }

        print(result);
    }

    @Override
    public void g(int n) {
        // G: 1, -4, 9, -16, 25....
        int[] result = new int[n];

        for (int k = 1; k < n + 1; k++) {
            result[k - 1] = k * k;
            result[k - 1] = (k % 2 == 0) ? (-1) * result[k - 1] : result[k - 1];
        }

        print(result);
    }

    @Override
    public void h(int n) {
        // H: 1, 0, 2, 0, 3, 0, 4....
        int[] result = new int[n];

        int inc = 1;
        for (int k = 1; k < n + 1; k++) {
            result[k - 1] = (k % 2 == 0) ? 0 : inc++;
        }

        print(result);
    }

    @Override
    public void i(int n) {
        //I: 1, 2, 6, 24, 120, 720...
        int[] result = new int[n];

        result[0] = 1;
        for (int k = 1; k < n; k++) {
            result[k] = result[k - 1] * (k + 1);
        }

        print(result);
    }

    @Override
    public void j(int n) {
        // J: 1, 1, 2, 3, 5, 8, 13, 21...
        int[] result = new int[n];

        result[0] = 1;
        result[1] = 1;
        for (int k = 2; k < n; k++) {
            result[k] = result[k - 1] + result[k - 2];
        }

        print(result);
    }
}

package io.ylab.intensive.lesson02.validator;

public class SnilsValidatorTest {
    public static void main(String[] args) {
        System.out.println(new SnilsValidatorImpl().validate("01468870570")); // false
        System.out.println(new SnilsValidatorImpl().validate("90114404441hf")); // false
        System.out.println(new SnilsValidatorImpl().validate("90114404441")); // true
        // строка с верным СНИЛС с незначащими пробелами по бокам пройдет валидацию
        System.out.println(new SnilsValidatorImpl().validate("90114404441 ")); // true

        System.out.println(new SnilsValidatorImpl().validate("")); // false
        System.out.println(new SnilsValidatorImpl().validate("fdg-3457")); // false
        System.out.println(new SnilsValidatorImpl().validate("  ruytu898///.z, zndgtr6435")); // false
        System.out.println(new SnilsValidatorImpl().validate(null)); // false
    }
}

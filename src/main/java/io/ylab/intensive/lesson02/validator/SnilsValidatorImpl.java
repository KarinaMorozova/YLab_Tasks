package io.ylab.intensive.lesson02.validator;

public class SnilsValidatorImpl implements SnilsValidator{
    public static final int FULL_NUMBER = 11;
    public static final int GENERATED_NUMBER = 9;

    private boolean isValid(String snils) {
        if (snils == null) { return false; }

        snils = snils.trim();
        if (snils.length() != FULL_NUMBER) { return false; }

        String lenCheck = snils.replaceAll("\\D+", "");
        if (lenCheck.length() != FULL_NUMBER) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validate(String snils) {
        if (!isValid(snils)) return false;

        boolean result = false;

        snils = snils.trim();
        char[] charArray = snils.toCharArray();
        int[] numArray = new int[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            numArray[i] = Character.digit(charArray[i], 10);
        }

        int sum = 0;
        for (int i = 0; i < GENERATED_NUMBER; i++) {
            sum = sum + (GENERATED_NUMBER - i) * numArray[i];
        }

        // вычисляем контрольный номер
        int controlNumber = 0;
        if (sum < 100) {
            controlNumber = sum;
        } else if (sum > 100) {
            int modulo = sum % 101;

            if (modulo != 100) {
                controlNumber = modulo;
            }
        }

        String snilsCheckNumber = snils.substring(GENERATED_NUMBER);
        if (controlNumber == Integer.parseInt(snilsCheckNumber)) {
            result = true;
        }

        return result;
    }
}

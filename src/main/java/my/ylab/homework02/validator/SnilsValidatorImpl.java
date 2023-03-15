package my.ylab.homework02.validator;

public class SnilsValidatorImpl implements SnilsValidator{
    public static final int FULL_NUMBER = 11;
    public static final int GENERATED_NUMBER = 9;

    @Override
    public boolean validate(String snils) {
        if (snils == null) { return false; }

        // Проверяем длину строки, убрав незначащие пробелы по бокам строки
        snils = snils.trim();
        if (snils.length() != FULL_NUMBER) { return false; }

        // убираем из строки все символы кроме цифр
        String lenCheck = snils.replaceAll("\\D+", "");
        if (lenCheck.length() != FULL_NUMBER) {
            return false;
        }

        boolean result = false;

        // переводим строку в массив целых чисел
        char[] charArray = snils.toCharArray();
        int[] numArray = new int[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            numArray[i] = Character.digit(charArray[i], 10);
        }

        // считаем сумму произведений элементов массива на 9, 8, .., 1
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

        // сравниваем контрольное число с 2-мя младшими разрядами СНИЛС
        String snilsCheckNumber = snils.substring(GENERATED_NUMBER);
        if (controlNumber == Integer.parseInt(snilsCheckNumber)) {
            result = true;
        }

        return result;
    }
}

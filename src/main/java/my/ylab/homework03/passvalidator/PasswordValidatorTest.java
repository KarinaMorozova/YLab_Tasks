package my.ylab.homework03.passvalidator;

public class PasswordValidatorTest {
    public static void main(String[] args) {
        System.out.println(PasswordValidator.validate("Mellissandra@", "2", "2")); // false

        System.out.println(PasswordValidator.validate("Olenna_Tyrell_from_House_of_Tyrell", "3", "3")); // false

        System.out.println(PasswordValidator.validate("Jon", "2**4", "2**4")); // false

        System.out.println(PasswordValidator.validate("Ramsey", "1234567890qwertyuiop", "1234567890qwertyuiop")); // false

        System.out.println(PasswordValidator.validate("Sansa", "12345", "54321")); // false

        System.out.println(PasswordValidator.validate("Arya", "Valar_Morghulis", "Valar_Morghulis")); // true
    }
}

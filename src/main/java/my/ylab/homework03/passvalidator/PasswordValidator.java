package my.ylab.homework03.passvalidator;

import my.ylab.homework03.passvalidator.exception.WrongPasswordException;
import my.ylab.homework03.passvalidator.exception.WrongLoginException;

public class PasswordValidator {
    public static boolean validate(String login, String password, String confirmPassword) {
        boolean result = true;

        String filter = "[a-zA-Z0-9_]*";

        try {
            if (login.replaceAll(filter, "").length() != 0) {
                throw new WrongLoginException("Логин содержит недопустимые символы");
            }

            if (login.length() >= 20) {
                throw new WrongLoginException("Логин слишком длинный");
            }

            if (password.replaceAll(filter, "").length() != 0) {
                throw new WrongPasswordException("Пароль содержит недопустимые символы");
            }

            if (password.length() >= 20) {
                throw new WrongPasswordException("Пароль слишком длинный");
            }

            if (!password.equals(confirmPassword)) {
                throw new WrongPasswordException("Пароль и подтверждение не совпадают");
            }
        }
        catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }

        return result;
    }
}

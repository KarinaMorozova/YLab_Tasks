package my.ylab.homework03.passvalidator.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(){}
    public WrongPasswordException(String msg) {
        super(msg);
    }
}

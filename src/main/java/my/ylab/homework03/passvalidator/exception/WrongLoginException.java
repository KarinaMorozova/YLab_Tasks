package my.ylab.homework03.passvalidator.exception;

public class WrongLoginException extends Exception {
    public WrongLoginException() {}
    public WrongLoginException(String msg) {
        super(msg);
    }
}

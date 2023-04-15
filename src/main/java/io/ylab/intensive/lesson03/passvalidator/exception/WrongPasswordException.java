package io.ylab.intensive.lesson03.passvalidator.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(){}
    public WrongPasswordException(String msg) {
        super(msg);
    }
}

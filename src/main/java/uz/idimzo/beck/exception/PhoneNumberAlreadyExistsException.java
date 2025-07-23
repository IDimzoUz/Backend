package uz.idimzo.beck.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }
}
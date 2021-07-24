package au.com.belong.phonenumbers.exception;

public class InvalidActivationRequestException extends RuntimeException {

    public InvalidActivationRequestException(String message) {
        super(message);
    }
}

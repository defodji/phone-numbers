package au.com.belong.phonenumbers.exception;

public class ExistingPhoneNumberException extends RuntimeException {

    public ExistingPhoneNumberException(String message) {
        super(message);
    }
}

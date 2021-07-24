package au.com.belong.phonenumbers.exception;

import au.com.belong.phonenumbers.models.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.reflect.MalformedParametersException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> handleCustomerNotFoundException(CustomerNotFoundException exc, HttpServletRequest request) {
        log.error("Exception {} Request {}", exc, request.getRequestURI());
        return new ResponseEntity<>(new ApiError().errorId("CUSTOMER_NOT_FOUND").message(exc.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExistingPhoneNumberException.class)
    public ResponseEntity<ApiError> handleExistingPhoneNumberException(ExistingPhoneNumberException exc, HttpServletRequest request) {
        log.error("Exception {} Request {}", exc, request.getRequestURI());
        return new ResponseEntity<>(new ApiError().errorId("NUMBER_ALREADY_ACTIVATED").message(exc.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidActivationRequestException.class)
    public ResponseEntity<ApiError> handleInvalidActivationRequestException(InvalidActivationRequestException exc, HttpServletRequest request) {
        log.error("Exception {} Request {}", exc, request.getRequestURI());
        return new ResponseEntity<>(new ApiError().errorId("INVALID_ACTIVATION_REQUEST").message(exc.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MalformedParametersException.class, ValidationException.class, MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleException(Exception exc, HttpServletRequest request) {
        log.error("Exception {} Request {}", exc, request.getRequestURI());
        return new ResponseEntity<>(new ApiError().errorId("BAD_REQUEST").message(exc.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

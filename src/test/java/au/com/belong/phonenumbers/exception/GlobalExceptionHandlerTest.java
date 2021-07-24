package au.com.belong.phonenumbers.exception;

import au.com.belong.phonenumbers.models.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler exceptionHandler;
    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleCustomerNotFoundException() {
        CustomerNotFoundException customerNotFoundException = mock(CustomerNotFoundException.class);
        ResponseEntity<ApiError> errorResponse = exceptionHandler.handleCustomerNotFoundException(customerNotFoundException, request);
        assertEquals(HttpStatus.CONFLICT, errorResponse.getStatusCode());
    }

    @Test
    void testHandleExistingPhoneNumberException() {
        ExistingPhoneNumberException phoneNumberException = mock(ExistingPhoneNumberException.class);
        ResponseEntity<ApiError> errorResponse = exceptionHandler.handleExistingPhoneNumberException(phoneNumberException, request);
        assertEquals(HttpStatus.CONFLICT, errorResponse.getStatusCode());
    }

    @Test
    void testHandleInvalidActivationRequestException() {
        InvalidActivationRequestException requestException = mock(InvalidActivationRequestException.class);
        ResponseEntity<ApiError> errorResponse = exceptionHandler.handleInvalidActivationRequestException(requestException, request);
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    }

    @Test
    void testHandleValidationException() {
        ConstraintViolationException violationException = mock(ConstraintViolationException.class);
        ResponseEntity<ApiError> errorResponse = exceptionHandler.handleException(violationException, request);
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    }
}
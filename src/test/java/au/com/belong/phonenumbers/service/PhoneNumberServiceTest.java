package au.com.belong.phonenumbers.service;

import au.com.belong.phonenumbers.exception.InvalidActivationRequestException;
import au.com.belong.phonenumbers.models.ActivationRequest;
import au.com.belong.phonenumbers.models.Customer;
import au.com.belong.phonenumbers.models.PhoneNumber;
import au.com.belong.phonenumbers.repository.PhoneNumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PhoneNumberServiceTest {
    private PhoneNumberService phoneNumberService;

    @Mock
    private PhoneNumberRepository phoneNumberRepository;

    @BeforeEach
    void setUp() {
        phoneNumberService = new PhoneNumberService(phoneNumberRepository);
    }

    @Test
    void testGetPhoneNumbers() {
        PhoneNumber number = getPhoneNumber();
        when(phoneNumberRepository.getPhoneNumbers(any(UUID.class))).thenReturn(List.of(number));
        List<PhoneNumber> phoneNumbers = phoneNumberService.getPhoneNumbers(UUID.fromString("03f84c05-e48b-4986-8275-e8674f956cd7"));
        assertEquals(1, phoneNumbers.size());
        assertEquals(number, phoneNumbers.get(0));
    }

    @Test
    void testActivatePhoneSuccess() {
        PhoneNumber phoneNumber = getPhoneNumber();
        when(phoneNumberRepository.createPhoneNumber(anyString(), any(UUID.class))).thenReturn(phoneNumber);
        assertEquals(phoneNumber,
                phoneNumberService.activatePhone(new ActivationRequest().number(phoneNumber.getNumber()).customerId(phoneNumber.getCustomer().getId())));
    }

    @Test
    void testActivatePhoneWhenInvalidRequest() {
        assertThrows(InvalidActivationRequestException.class,
                () -> phoneNumberService.activatePhone(new ActivationRequest()));
        assertThrows(InvalidActivationRequestException.class,
                () -> phoneNumberService.activatePhone(null));
        assertThrows(InvalidActivationRequestException.class,
                () -> phoneNumberService.activatePhone(new ActivationRequest().number("0420453555")));
    }

    private PhoneNumber getPhoneNumber() {
        return new PhoneNumber()
                .number("0420444555")
                .customer(new Customer().id(UUID.randomUUID()).name("John Doe"));
    }
}
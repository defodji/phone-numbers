package au.com.belong.phonenumbers.repository;

import au.com.belong.phonenumbers.exception.CustomerNotFoundException;
import au.com.belong.phonenumbers.exception.ExistingPhoneNumberException;
import au.com.belong.phonenumbers.models.PhoneNumber;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static au.com.belong.phonenumbers.repository.PhoneNumberRepository.PHONE_NUMBER_TABLE;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PhoneNumberRepositoryTest {
    private PhoneNumberRepository phoneNumberRepository;

    @BeforeEach
    void setUp() {
        phoneNumberRepository = new PhoneNumberRepository();
    }

    @Test
    @Order(1)
    void testGetAllPhoneNumbers() {
        List<PhoneNumber> numbers = phoneNumberRepository.getPhoneNumbers(null);
        assertEquals(PHONE_NUMBER_TABLE.size(), numbers.size());
    }

    @Test
    @Order(2)
    void testGetPhoneNumbersForExistingCustomer() {
        List<PhoneNumber> numbers = phoneNumberRepository.getPhoneNumbers(UUID.fromString("03f84c05-e48b-4986-8275-e8674f956cd7"));
        assertEquals(2, numbers.size());
    }

    @Test
    @Order(3)
    void testCreatePhoneNumberWhenCustomerNotFound() {
        assertThrows(CustomerNotFoundException.class, () -> phoneNumberRepository.createPhoneNumber("0245678165", UUID.fromString("92f84c05-e48b-4444-8275-e8674f954444")));
    }

    @Test
    @Order(4)
    void testCreatePhoneNumberWhenExistingPhoneNumber() {
        assertThrows(ExistingPhoneNumberException.class, () -> phoneNumberRepository.createPhoneNumber("0245678945", UUID.fromString("02f84c05-e48b-4986-8275-e8674f956cd6")));
    }

    @Test
    @Order(5)
    void testCreatePhoneNumberSuccess() {
        PhoneNumber phoneNumber = phoneNumberRepository.createPhoneNumber("0245678333", UUID.fromString("02f84c05-e48b-4986-8275-e8674f956cd6"));

        assertEquals("0245678333", phoneNumber.getNumber());
        assertEquals("02f84c05-e48b-4986-8275-e8674f956cd6", phoneNumber.getCustomer().getId().toString());

        List<PhoneNumber> numbers = phoneNumberRepository.getPhoneNumbers(UUID.fromString("02f84c05-e48b-4986-8275-e8674f956cd6"));
        assertEquals(2, numbers.size());
    }
}
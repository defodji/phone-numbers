package au.com.belong.phonenumbers.repository;

import au.com.belong.phonenumbers.exception.CustomerNotFoundException;
import au.com.belong.phonenumbers.exception.ExistingPhoneNumberException;
import au.com.belong.phonenumbers.models.Customer;
import au.com.belong.phonenumbers.models.PhoneNumber;
import au.com.belong.phonenumbers.dto.PhoneNumberDto;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PhoneNumberRepository {
    public static List<PhoneNumberDto> PHONE_NUMBER_TABLE = Lists.newArrayList(
            PhoneNumberDto.builder().number("0245678945").customerId("02f84c05-e48b-4986-8275-e8674f956cd6").build(),
            PhoneNumberDto.builder().number("0435678945").customerId("03f84c05-e48b-4986-8275-e8674f956cd7").build(),
            PhoneNumberDto.builder().number("0455678955").customerId("03f84c05-e48b-4986-8275-e8674f956cd7").build()
    );
    public static List<Customer> CUSTOMER_TABLE = List.of(
            createCustomer("02f84c05-e48b-4986-8275-e8674f956cd6", "John Doe"),
            createCustomer("03f84c05-e48b-4986-8275-e8674f956cd7", "Sarah Gigi")
    );

    private static Customer createCustomer(String id, String name) {
        return new Customer().id(UUID.fromString(id)).name(name);
    }

    public List<PhoneNumber> getPhoneNumbers(UUID customerId) {
        return PHONE_NUMBER_TABLE.stream().filter(ph -> meetCriteria(ph, customerId)).map(ph -> convert(ph)).collect(Collectors.toList());
    }

    public PhoneNumber createPhoneNumber(String number, UUID customerId) {
        boolean numberExists = PHONE_NUMBER_TABLE.stream().anyMatch(p -> p.getNumber().equals(number));
        if (numberExists) {
            throw new ExistingPhoneNumberException(number + " is already activated");
        }
        Customer customer = CUSTOMER_TABLE.stream().filter(c -> c.getId().equals(customerId)).findFirst().orElse(null);
        if (customer != null) {
            synchronized(this) {
                PhoneNumber phoneNumber = new PhoneNumber().number(number).customer(customer);
                PHONE_NUMBER_TABLE.add(
                            PhoneNumberDto.builder()
                                    .number(phoneNumber.getNumber())
                                    .customerId(customerId.toString()).build());
                return phoneNumber;
            }
        } else {
            throw new CustomerNotFoundException("Customer Id" + customerId + "does not exist");
        }
    }

    private boolean meetCriteria(PhoneNumberDto phoneNumberDto, UUID customerId) {
        return (ObjectUtils.isEmpty(customerId) || UUID.fromString(phoneNumberDto.getCustomerId()).equals(customerId));
    }

    private PhoneNumber convert(PhoneNumberDto phoneNumberDto) {
        return new PhoneNumber()
                .number(phoneNumberDto.getNumber())
                .customer(CUSTOMER_TABLE.stream().filter(cust -> cust.getId().equals(UUID.fromString(phoneNumberDto.getCustomerId()))).findFirst().orElseThrow());
    }

}

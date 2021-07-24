package au.com.belong.phonenumbers.service;

import au.com.belong.phonenumbers.exception.InvalidActivationRequestException;
import au.com.belong.phonenumbers.models.ActivationRequest;
import au.com.belong.phonenumbers.models.PhoneNumber;
import au.com.belong.phonenumbers.repository.PhoneNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneNumberService {
    private final PhoneNumberRepository phoneNumberRepository;

    public List<PhoneNumber> getPhoneNumbers(UUID customerId) {
        return phoneNumberRepository.getPhoneNumbers(customerId);
    }

    public PhoneNumber activatePhone(ActivationRequest request) {
        if (request == null || request.getNumber() == null || request.getCustomerId() == null) {
            throw new InvalidActivationRequestException("Missing information in activation request");
        }
        return phoneNumberRepository.createPhoneNumber(request.getNumber(), request.getCustomerId());
    }
}

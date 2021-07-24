package au.com.belong.phonenumbers.controller;

import au.com.belong.phonenumbers.api.V1Api;
import au.com.belong.phonenumbers.models.ActivationRequest;
import au.com.belong.phonenumbers.models.PhoneNumber;
import au.com.belong.phonenumbers.service.PhoneNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class PhoneNumbersController implements V1Api {
    private final PhoneNumberService phoneNumberService;

    @PostMapping("/v1/phonenumbers")
    public ResponseEntity<PhoneNumber> activatePhoneNumber(@Valid @RequestBody ActivationRequest activationRequest) {
        return new ResponseEntity<>(phoneNumberService.activatePhone(activationRequest), HttpStatus.CREATED);
    }

    @GetMapping("/v1/phonenumbers")
    public ResponseEntity<List<PhoneNumber>> listPhoneNumbers(@Valid @RequestParam(value = "customerId", required = false) UUID customerId) {
        return new ResponseEntity<>(phoneNumberService.getPhoneNumbers(customerId), HttpStatus.OK);
    }
}

package au.com.belong.phonenumbers.dto;

import lombok.*;


@Builder
@Getter
@AllArgsConstructor
public class PhoneNumberDto {
    private String number;
    private String customerId;
}

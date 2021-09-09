package ruhavarbackend.phonenumber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruhavarbackend.customer.Customer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumberDTO {

    private Long id;

    private String type;

    private String number;

    private Customer customer;
}

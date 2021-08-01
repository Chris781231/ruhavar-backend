package ruhavarbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruhavarbackend.entity.PhoneNumber;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long id;

    private String name;

    private String city;

    private String address;

    private List<PhoneNumber> phoneNumbers;

    private String email;
}

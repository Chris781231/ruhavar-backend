package ruhavarbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "cus_gen")
//    @TableGenerator(name = "cus_gen", table = "cus_id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val")
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    private String address;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    private String email;

    public Customer(String name, String city, String address, String email) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.email = email;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
        phoneNumber.setCustomer(this);
    }
}

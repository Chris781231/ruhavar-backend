package ruhavarbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "customers")
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

    private String phoneNumber;

    private String email;

    public Customer(String name, String city, String address, String phoneNumber, String email) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}

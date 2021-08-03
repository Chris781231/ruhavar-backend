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

    // TODO: 2021. 08. 03. Change id generation by identity to table, to do this it must be created a flyway migration file for tablegenerator
//    @GeneratedValue(generator = "cus_gen")
//    @TableGenerator(name = "cus_gen", table = "cus_id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}

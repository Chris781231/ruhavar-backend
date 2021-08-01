package ruhavarbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
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

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @ToString.Exclude
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    private String email;

    public Customer(String name, String city, String address) {
        this.name = name;
        this.city = city;
        this.address = address;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
        phoneNumber.setCustomer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;

        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return 339958611;
    }
}

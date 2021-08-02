package ruhavarbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phonenumbers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String number;

    @ManyToOne
    @JoinColumn(name = "customers_id")
    @JsonBackReference
    private Customer customer;

    public PhoneNumber(String type, String number) {
        this.type = type;
        this.number = number;
    }
}
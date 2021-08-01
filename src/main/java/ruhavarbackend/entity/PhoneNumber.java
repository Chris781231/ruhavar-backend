package ruhavarbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phonenumbers")
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhoneNumber that = (PhoneNumber) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 564916499;
    }
}
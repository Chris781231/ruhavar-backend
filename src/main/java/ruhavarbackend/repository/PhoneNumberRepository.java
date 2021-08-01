package ruhavarbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruhavarbackend.entity.PhoneNumber;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}

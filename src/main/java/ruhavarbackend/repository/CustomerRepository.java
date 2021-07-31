package ruhavarbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ruhavarbackend.entity.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameIsContainingIgnoreCase(String name);
}

package ruhavarbackend.customer;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruhavarbackend.customer.dto.CreateCustomerCommand;
import ruhavarbackend.customer.dto.UpdateCustomerCommand;
import ruhavarbackend.customer.dto.CustomerDTO;
import ruhavarbackend.exception.EntityNotFoundException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    public static final String CUSTOMER = "customer";

    private CustomerRepository customerRepo;

    private ModelMapper modelMapper;

    public CustomerDTO saveCustomer(CreateCustomerCommand command) {
        Customer customer = new Customer(
                command.getName(), command.getCity(), command.getAddress(), command.getEmail());
        Customer savedCustomer = customerRepo.save(customer);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    public List<CustomerDTO> listCustomers(Optional<String> name) {
        List<Customer> customers;
        if (name.isPresent()) {
            customers = customerRepo.findByNameIsContainingIgnoreCase(name.get());
        } else {
            customers = customerRepo.findAll();
        }

        Type targetType = new TypeToken<List<CustomerDTO>>(){}.getType();
        return modelMapper.map(customers, targetType);
    }

    public CustomerDTO findById(long id) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id, CUSTOMER));
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Transactional
    public CustomerDTO updateCustomerById(long id, UpdateCustomerCommand command) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, CUSTOMER));
        customer.setName(command.getName());
        customer.setCity(command.getCity());
        customer.setAddress(command.getAddress());
        customer.setEmail(command.getEmail());
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public void deleteAll() {
        customerRepo.deleteAll();
    }

    public void deleteById(long id) {
        if (!customerRepo.existsById(id)) {
            throw new EntityNotFoundException(id, CUSTOMER);
        }
        customerRepo.deleteById(id);
    }
}

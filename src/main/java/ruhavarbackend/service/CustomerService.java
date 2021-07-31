package ruhavarbackend.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruhavarbackend.command.CreateCustomerCommand;
import ruhavarbackend.command.UpdateCustomerCommand;
import ruhavarbackend.dto.CustomerDTO;
import ruhavarbackend.entity.Customer;
import ruhavarbackend.exception.CustomerNotFoundException;
import ruhavarbackend.repository.CustomerRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepo;

    private ModelMapper modelMapper;

    public CustomerDTO saveCustomer(CreateCustomerCommand command) {
        Customer customer = new Customer(
                command.getName(), command.getCity(), command.getAddress(), command.getPhoneNumber(), command.getEmail());
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
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Transactional
    public CustomerDTO updateCustomerById(long id, UpdateCustomerCommand command) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setName(command.getName());
        customer.setCity(command.getCity());
        customer.setAddress(command.getAddress());
        customer.setPhoneNumber(command.getPhoneNumber());
        customer.setEmail(command.getEmail());
        return modelMapper.map(customer, CustomerDTO.class);

    }

    public void deleteAll() {
        customerRepo.deleteAll();
    }

    public void deleteById(long id) {
        if (!customerRepo.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepo.deleteById(id);
    }
}

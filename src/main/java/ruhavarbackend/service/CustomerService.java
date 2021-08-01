package ruhavarbackend.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruhavarbackend.command.CreateCustomerCommand;
import ruhavarbackend.command.UpdateCustomerCommand;
import ruhavarbackend.command.AddPhoneNumberCommand;
import ruhavarbackend.dto.CustomerDTO;
import ruhavarbackend.entity.Customer;
import ruhavarbackend.entity.PhoneNumber;
import ruhavarbackend.exception.CustomerNotFoundException;
import ruhavarbackend.repository.CustomerRepository;
import ruhavarbackend.repository.PhoneNumberRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepo;

    private PhoneNumberRepository phoneNumberRepo;

    private ModelMapper modelMapper;

    public CustomerDTO saveCustomer(CreateCustomerCommand command) {
        Customer customer = new Customer(
                command.getName(), command.getCity(), command.getAddress());
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
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Transactional
    public CustomerDTO addPhoneNumberById(long id, AddPhoneNumberCommand command) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        PhoneNumber phoneNumber = new PhoneNumber(command.getType(), command.getNumber());
        customer.addPhoneNumber(phoneNumber);
        phoneNumberRepo.save(phoneNumber);
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

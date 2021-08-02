package ruhavarbackend.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruhavarbackend.command.CreatePhoneNumberCommand;
import ruhavarbackend.command.UpdatePhoneNumberCommand;
import ruhavarbackend.dto.PhoneNumberDTO;
import ruhavarbackend.entity.Customer;
import ruhavarbackend.entity.PhoneNumber;
import ruhavarbackend.exception.CustomerNotFoundException;
import ruhavarbackend.exception.PhoneNumberNotFoundException;
import ruhavarbackend.repository.CustomerRepository;
import ruhavarbackend.repository.PhoneNumberRepository;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class PhoneNumberService {

    private PhoneNumberRepository phoneNumberRepo;

    private CustomerRepository customerRepo;

    private ModelMapper modelMapper;

    public List<PhoneNumberDTO> listPhoneNumbers() {
        List<PhoneNumber> phoneNumbers = phoneNumberRepo.findAll();
        Type targetType = new TypeToken<List<PhoneNumberDTO>>(){}.getType();
        return modelMapper.map(phoneNumbers, targetType);
    }

    public PhoneNumberDTO findPhoneNumberById(long id) {
        PhoneNumber phoneNumber = phoneNumberRepo.findById(id).orElseThrow(() -> new PhoneNumberNotFoundException(id));
        return modelMapper.map(phoneNumber, PhoneNumberDTO.class);
    }

    public PhoneNumberDTO savePhoneNumber(long id, CreatePhoneNumberCommand command) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        PhoneNumber phoneNumber = new PhoneNumber(command.getType(), command.getNumber());
        phoneNumber.setCustomer(customer);
        phoneNumberRepo.save(phoneNumber);
        return modelMapper.map(phoneNumber, PhoneNumberDTO.class);
    }

    @Transactional
    public PhoneNumberDTO updatePhoneNumberById(long id, UpdatePhoneNumberCommand command) {
        PhoneNumber phoneNumber = phoneNumberRepo.findById(id).orElseThrow(() -> new PhoneNumberNotFoundException(id));
        phoneNumber.setNumber(command.getNumber());
        return modelMapper.map(phoneNumber, PhoneNumberDTO.class);
    }

    public void deleteById(long id) {
        phoneNumberRepo.deleteById(id);
    }

    public void deleteAll() {
        phoneNumberRepo.deleteAll();
    }
}

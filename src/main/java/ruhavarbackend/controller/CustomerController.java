package ruhavarbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ruhavarbackend.command.CreateCustomerCommand;
import ruhavarbackend.command.UpdateCustomerCommand;
import ruhavarbackend.command.AddPhoneNumberCommand;
import ruhavarbackend.dto.CustomerDTO;
import ruhavarbackend.service.CustomerService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
@Tag(name = "Operations on customers")
public class CustomerController {

    private CustomerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a customer")
    @ApiResponse(responseCode = "201", description = "Customer has been created")
    public CustomerDTO saveCustomer(@RequestBody @Valid CreateCustomerCommand command) {
        return service.saveCustomer(command);
    }

    @GetMapping
    @Operation(summary = "List customers (optionally by name)")
    public List<CustomerDTO> listCustomers(@RequestParam Optional<String> name) {
        return service.listCustomers(name);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds a customer by id")
    public CustomerDTO findCustomerById(@PathVariable long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a customer by id")
    public CustomerDTO updateCustomerById(@PathVariable long id, @RequestBody @Valid UpdateCustomerCommand command) {
        return service.updateCustomerById(id, command);
    }

    @DeleteMapping
    @Operation(summary = "Delete all of customers")
    public void deleteAll() {
        service.deleteAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a customer by id")
    public void deleteByID(@PathVariable long id) {
        service.deleteById(id);
    }
}

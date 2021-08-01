package ruhavarbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ruhavarbackend.command.CreatePhoneNumberCommand;
import ruhavarbackend.command.UpdatePhoneNumberCommand;
import ruhavarbackend.dto.CustomerDTO;
import ruhavarbackend.dto.PhoneNumberDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/phonenumbers")
@AllArgsConstructor
@Tag(name = "Operations on phonenumbers")
public class PhoneNumberController {

    private PhoneNumberService service;

    @GetMapping
    @Operation(summary = "List phonenumbers")
    public List<PhoneNumberDTO> listPhoneNumbers() {
        return service.listPhoneNumbers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a phonenumber")
    public PhoneNumberDTO findPhoneNumberById(@PathVariable long id) {
        return service.findPhoneNumberById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update number of a phonenumber")
    public PhoneNumberDTO updatePhoneNumberById(
            @PathVariable long id,
            @RequestBody @Valid UpdatePhoneNumberCommand command) {
        return service.updatePhoneNumberById(id, command);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a phonenumber")
    @ApiResponse(responseCode = "201", description = "Phonenumber has been created")
    public PhoneNumberDTO savePhoneNumber(@RequestBody @Valid CreatePhoneNumberCommand command) {
        return service.savePhoneNumber(command);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a phonenumber by id")
    @ApiResponse(description = "Phonenumber has been deleted")
    public void deleteById(@PathVariable long id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @Operation(summary = "Delete all of phonenumbers")
    @ApiResponse(description = "Phonenumbers have been deleted")
    public void deleteAll() {
        service.deleteAll();
    }
}

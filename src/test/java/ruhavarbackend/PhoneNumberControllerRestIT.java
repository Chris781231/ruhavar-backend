package ruhavarbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.zalando.problem.Problem;
import ruhavarbackend.command.*;
import ruhavarbackend.dto.CustomerDTO;
import ruhavarbackend.dto.PhoneNumberDTO;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhoneNumberControllerRestIT {

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    void init() {
        template.delete("/api/phonenumbers");
    }

    @Test
    void saveCustomerThenListAllTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Budapest", "Kossuth utca 2", ""),
                CustomerDTO.class);
        template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("home", "19876543"),
                PhoneNumberDTO.class);

        List<PhoneNumberDTO> result = template.exchange("/api/phonenumbers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PhoneNumberDTO>>() {})
                .getBody();

        assertEquals(2, Objects.requireNonNull(result).size());
    }

    @Test
    void findPhoneNumberByIdTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Budapest", "Kossuth utca 2", ""),
                CustomerDTO.class);
        PhoneNumberDTO cell = template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        PhoneNumberDTO result = template.exchange("/api/phonenumbers/" + cell.getId(),
                HttpMethod.GET,
                null,
                PhoneNumberDTO.class).getBody();

        assertEquals("701234567", Objects.requireNonNull(result).getNumber());
    }

    @Test
    void updateNumberOfPhoneNumberTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Budapest", "Kossuth utca 2", ""),
                CustomerDTO.class);
        PhoneNumberDTO cell = template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        template.put("/api/phonenumbers/" + cell.getId(),
                new UpdatePhoneNumberCommand("209876543"));

        PhoneNumberDTO result = template.exchange("/api/phonenumbers/" + cell.getId(),
                HttpMethod.GET,
                null,
                PhoneNumberDTO.class).getBody();

        assertEquals("209876543", result.getNumber());
    }

    @Test
    void deleteByIdTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Budapest", "Kossuth utca 2", ""),
                CustomerDTO.class);
        PhoneNumberDTO cell = template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        template.delete("/api/phonenumbers/" + cell.getId());
        List<PhoneNumberDTO> result = template.exchange("/api/phonenumbers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PhoneNumberDTO>>() {})
                .getBody();

        assertTrue(Objects.requireNonNull(result).isEmpty());
    }

    @Test
    void saveInvalidCustomerTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Budapest", "Kossuth utca 2", ""),
                CustomerDTO.class);
        Problem blankTypeProblem = template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("", "701234567"),
                Problem.class);
        Problem blankNumberProblem = template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("home", ""),
                Problem.class);

        assertEquals(400, blankTypeProblem.getStatus().getStatusCode());
        assertEquals(400, blankNumberProblem.getStatus().getStatusCode());
    }

    @Test
    void updateInvalidNumberOfPhoneNumberTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Budapest", "Kossuth utca 2", ""),
                CustomerDTO.class);
        template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);

        Problem blankNumberProblem = template.exchange("/api/phonenumbers/" + customerDTO.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(new UpdatePhoneNumberCommand("")),
                Problem.class).getBody();

        assertEquals(400, blankNumberProblem.getStatus().getStatusCode());
    }

    @Test
    void addPhoneNumberTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);
        template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new AddPhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        CustomerDTO customerWithPhoneNumber = template.exchange("/api/customers/" + customerDTO.getId(),
                HttpMethod.GET,
                null,
                CustomerDTO.class).getBody();

        assertEquals("701234567", customerWithPhoneNumber.getPhoneNumbers().get(0).getNumber());
    }

    @Test
    void addInvalidPhoneNumberTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);
        Problem blankTypeProblem = template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new AddPhoneNumberCommand("", "701234567"),
                Problem.class);
        Problem blankNumberProblem = template.postForObject("/api/phonenumbers/" + customerDTO.getId(),
                new AddPhoneNumberCommand("cell", ""),
                Problem.class);

        assertEquals(400,blankTypeProblem.getStatus().getStatusCode());
        assertEquals(400,blankNumberProblem.getStatus().getStatusCode());
    }
}

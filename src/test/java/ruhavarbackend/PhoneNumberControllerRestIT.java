package ruhavarbackend;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.zalando.problem.Problem;
import ruhavarbackend.command.CreateCustomerCommand;
import ruhavarbackend.command.CreatePhoneNumberCommand;
import ruhavarbackend.command.UpdateCustomerCommand;
import ruhavarbackend.command.UpdatePhoneNumberCommand;
import ruhavarbackend.dto.CustomerDTO;
import ruhavarbackend.dto.PhoneNumberDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhoneNumberControllerRestIT {

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    void init() throws SQLException {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        dataSource.setUrl("jdbc:mariadb://localhost:3309/ruhavar?useUnicode=true");
        dataSource.setUser("ruhavar");
        dataSource.setPassword("ruhavar");

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();

        flyway.clean();
        flyway.migrate();

        template.delete("/api/phonenumbers");
    }

    @Test
    void saveCustomerThenListAllTest() {
        template.postForObject("/api/phonenumbers",
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        template.postForObject("/api/phonenumbers",
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
        template.postForObject("/api/phonenumbers",
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        PhoneNumberDTO result = template.exchange("/api/phonenumbers/1",
                HttpMethod.GET,
                null,
                PhoneNumberDTO.class).getBody();

        assertEquals("701234567", Objects.requireNonNull(result).getNumber());
    }

    @Test
    void updateNumberOfPhoneNumberTest() {
        template.postForObject("/api/phonenumbers",
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        template.put("/api/phonenumbers/1",
                new UpdatePhoneNumberCommand("209876543"));

        PhoneNumberDTO result = template.exchange("/api/phonenumbers/1",
                HttpMethod.GET,
                null,
                PhoneNumberDTO.class).getBody();

        assertEquals("209876543", result.getNumber());
    }

    @Test
    void deleteByIdTest() {
        template.postForObject("/api/phonenumbers",
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);
        template.delete("/api/phonenumbers/1");
        List<PhoneNumberDTO> result = template.exchange("/api/phonenumbers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PhoneNumberDTO>>() {})
                .getBody();

        assertTrue(Objects.requireNonNull(result).isEmpty());
    }

    @Test
    void saveInvalidCustomerTest() {
        Problem blankTypeProblem = template.postForObject("/api/phonenumbers",
                new CreatePhoneNumberCommand("", "701234567"),
                Problem.class);
        Problem blankNumberProblem = template.postForObject("/api/phonenumbers",
                new CreatePhoneNumberCommand("home", ""),
                Problem.class);

        assertEquals(400, blankTypeProblem.getStatus().getStatusCode());
        assertEquals(400, blankNumberProblem.getStatus().getStatusCode());
    }

    @Test
    void updateInvalidNumberOfPhoneNumberTest() {
        template.postForObject("/api/phonenumbers",
                new CreatePhoneNumberCommand("cell", "701234567"),
                PhoneNumberDTO.class);

        Problem blankNumberProblem = template.exchange("/api/phonenumbers/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdatePhoneNumberCommand("")),
                Problem.class).getBody();

        assertEquals(400, blankNumberProblem.getStatus().getStatusCode());
    }
}

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
import ruhavarbackend.command.AddPhoneNumberCommand;
import ruhavarbackend.command.CreateCustomerCommand;
import ruhavarbackend.command.UpdateCustomerCommand;
import ruhavarbackend.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerRestIT {

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

        template.delete("/api/customers");
    }

    @Test
    void saveCustomerThenListAllTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége köz sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Jóska", "Alsóbögyörősvalagpuszta", "Bárakárhol", "domain@mail.com"),
                CustomerDTO.class);

        List<CustomerDTO> result = template.exchange("/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {})
                .getBody();

        assertEquals(2, Objects.requireNonNull(result).size());
    }

    @Test
    void findByNameTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Jóska", "Alsóbögyörősvalagpuszta", "Bárakárhol", "domain@mail.com"),
                CustomerDTO.class);

        List<CustomerDTO> result = template.exchange("/api/customers?name=Teszt Jóska",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {})
                .getBody();

        assertEquals("Teszt Jóska", Objects.requireNonNull(result).get(0).getName());
    }

    @Test
    void updateCustomerTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);

        template.put("/api/customers/1",
                new UpdateCustomerCommand("Teszt Jóska", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"));

        CustomerDTO result = template.exchange("/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {
                })
                .getBody().get(0);

        assertEquals("Teszt Jóska", result.getName());
    }

    @Test
    void saveCustomerWithInvalidNameCityAndAddressTest() {
        Problem nameProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                Problem.class);

        Problem cityProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "", "Világvége sarkon jobbra", "domain@mail.com"),
                Problem.class);

        Problem addressProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "", "domain@mail.com"),
                Problem.class);

        assertEquals(400, nameProblem.getStatus().getStatusCode());
        assertEquals(400, cityProblem.getStatus().getStatusCode());
        assertEquals(400, addressProblem.getStatus().getStatusCode());
    }

    @Test
    void findCustomerWithInvalidIdTest() {
        Problem customerNotFoundProblem = template.exchange("/api/customers/1",
                HttpMethod.GET,
                null,
                Problem.class).getBody();

        assertEquals(404,customerNotFoundProblem.getStatus().getStatusCode());
    }

    @Test
    void updateCustomerWithInvalidIdTest() {
        Problem customerNotFoundProblem = template.exchange("/api/customer/2",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("Teszt Jóska", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com")),
                Problem.class).getBody();

        assertEquals(404,customerNotFoundProblem.getStatus().getStatusCode());
    }

    @Test
    void updateCustomerWithBlankAttributesTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);

        Problem blankNameProblem = template.exchange("/api/customers/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com")),
                Problem.class).getBody();
        Problem blankCityProblem = template.exchange("/api/customers/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("Teszt Jóska", "", "Világvége sarkon jobbra", "domain@mail.com")),
                Problem.class).getBody();
        Problem blankAddressProblem = template.exchange("/api/customers/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("Teszt Jóska", "Karakószörcsög", "", "domain@mail.com")),
                Problem.class).getBody();

        assertEquals(400,blankNameProblem.getStatus().getStatusCode());
        assertEquals(400,blankCityProblem.getStatus().getStatusCode());
        assertEquals(400,blankAddressProblem.getStatus().getStatusCode());
    }

    @Test
    void deleteByIdTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);

        template.delete("/api/customers/1");

        List<CustomerDTO> result = template.exchange("/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {
                }).getBody();

        assertEquals(0, result.size());
    }

    @Test
    void deleteByInvalidIdTest() {
        Problem problem = template.exchange("/api/customers/1",
                HttpMethod.DELETE,
                null,
                Problem.class).getBody();

        assertEquals(404, problem.getStatus().getStatusCode());
    }

    @Test
    void addPhoneNumberTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);
        template.put("/api/customers/1/addphonenumber",
                new AddPhoneNumberCommand("cell", "701234567"));
        CustomerDTO customerWithPhoneNumber = template.exchange("/api/customers/1",
                HttpMethod.GET,
                null,
                CustomerDTO.class).getBody();

        assertEquals("701234567", customerWithPhoneNumber.getPhoneNumbers().get(0).getNumber());
    }

    @Test
    void addInvalidPhoneNumberTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "domain@mail.com"),
                CustomerDTO.class);
        Problem blankTypeProblem = template.exchange("/api/customers/1/addphonenumber",
                HttpMethod.PUT,
                new HttpEntity<>(new AddPhoneNumberCommand("", "701234567")),
                Problem.class).getBody();
        Problem blankNumberProblem = template.exchange("/api/customers/1/addphonenumber",
                HttpMethod.PUT,
                new HttpEntity<>(new AddPhoneNumberCommand("cell", "")),
                Problem.class).getBody();

        assertEquals(400,blankTypeProblem.getStatus().getStatusCode());
        assertEquals(400,blankNumberProblem.getStatus().getStatusCode());
    }
}

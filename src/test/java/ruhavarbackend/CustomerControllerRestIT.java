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
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége köz sarkon jobbra", "91123456", "mail@domain.hu"),
                CustomerDTO.class);
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Jóska", "Alsóbögyörősvalagpuszta", "Bárakárhol", "90987654", "ilyenmarcsaknincs@gmail.com"),
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
                new CreateCustomerCommand("Pista", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu"),
                CustomerDTO.class);
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Jóska", "Alsóbögyörősvalagpuszta", "Bárakárhol", "90987654", "ilyenmarcsaknincs@gmail.com"),
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
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu"),
                CustomerDTO.class);

        template.put("/api/customers/1",
                new UpdateCustomerCommand("Teszt Jóska", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu"));

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
                new CreateCustomerCommand("", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu"),
                Problem.class);

        Problem cityProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "", "Világvége sarkon jobbra", "91123456", "mail@domain.hu"),
                Problem.class);

        Problem addressProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "", "91123456", "mail@domain.hu"),
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
    void updateCustomerWithInvalidId() {
        Problem customerNotFoundProblem = template.exchange("/api/customer/2",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("Teszt Jóska", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu")),
                Problem.class).getBody();

        assertEquals(404,customerNotFoundProblem.getStatus().getStatusCode());
    }

    @Test
    void updateCustomerWithBlankAttributes() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu"),
                CustomerDTO.class);

        Problem blankNameProblem = template.exchange("/api/customers/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu")),
                Problem.class).getBody();
        Problem blankCityProblem = template.exchange("/api/customers/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("Teszt Jóska", "", "Világvége sarkon jobbra", "91123456", "mail@domain.hu")),
                Problem.class).getBody();
        Problem blankAddressProblem = template.exchange("/api/customers/1",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("Teszt Jóska", "Karakószörcsög", "", "91123456", "mail@domain.hu")),
                Problem.class).getBody();

        assertEquals(400,blankNameProblem.getStatus().getStatusCode());
        assertEquals(400,blankCityProblem.getStatus().getStatusCode());
        assertEquals(400,blankAddressProblem.getStatus().getStatusCode());
    }

    @Test
    void deleteById() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Teszt Pista", "Karakószörcsög", "Világvége sarkon jobbra", "91123456", "mail@domain.hu"),
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
    void deleteByInvalidId() {
        Problem problem = template.exchange("/api/customers/1",
                HttpMethod.DELETE,
                null,
                Problem.class).getBody();

        assertEquals(404, problem.getStatus().getStatusCode());
    }
}

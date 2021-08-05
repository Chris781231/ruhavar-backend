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
import ruhavarbackend.command.CreateCustomerCommand;
import ruhavarbackend.command.UpdateCustomerCommand;
import ruhavarbackend.dto.CustomerDTO;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerRestIT {

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    void init() {
        template.delete("/api/customers");
    }

    @Test
    void saveCustomerThenListAllTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("John Doe", "Budapest", "Kossuth utca 2", "name@domain.com"),
                CustomerDTO.class);
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Jane Doe", "Budapest", "Széchenyi István tér 4", "anothername@domain.com"),
                CustomerDTO.class);

        List<CustomerDTO> result = template.exchange("/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {})
                .getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(CustomerDTO::getName, CustomerDTO::getCity, CustomerDTO::getAddress, CustomerDTO::getEmail)
                .contains(tuple("Jane Doe", "Budapest", "Széchenyi István tér 4", "anothername@domain.com"));
    }

    @Test
    void findByNameTest() {
        template.postForObject("/api/customers",
                new CreateCustomerCommand("John Doe", "Budapest", "Kossuth utca 2", "name@domain.com"),
                CustomerDTO.class);
        template.postForObject("/api/customers",
                new CreateCustomerCommand("Jane Doe", "Budapest", "Széchenyi István tér 4", "anothername@domain.com"),
                CustomerDTO.class);

        List<CustomerDTO> result = template.exchange("/api/customers?name=Jane Doe",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {})
                .getBody();

        assertEquals("Széchenyi István tér 4", Objects.requireNonNull(result).get(0).getAddress());
    }

    @Test
    void updateCustomerTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("John Doe", "Budapest", "Kossuth utca 2", "name@domain.com"),
                CustomerDTO.class);

        template.put("/api/customers/" + customerDTO.getId(),
                new UpdateCustomerCommand("Jane Doe", "Budapest", "Széchenyi István tér 4", "anothername@domain.com"));

        CustomerDTO result = template.exchange("/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {
                })
                .getBody().get(0);

        assertEquals("Jane Doe", result.getName());
    }

    @Test
    void saveCustomerWithInvalidNameCityAndAddressTest() {
        Problem nameProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("", "Budapest", "Széchenyi István tér 4", "anothername@domain.com"),
                Problem.class);

        Problem cityProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("John Doe", "", "Széchenyi István tér 4", "anothername@domain.com"),
                Problem.class);

        Problem addressProblem = template.postForObject("/api/customers",
                new CreateCustomerCommand("John Doe", "Budapest", "", "anothername@domain.com"),
                Problem.class);

        assertEquals(400, nameProblem.getStatus().getStatusCode());
        assertEquals(400, cityProblem.getStatus().getStatusCode());
        assertEquals(400, addressProblem.getStatus().getStatusCode());
    }

    @Test
    void findCustomerWithInvalidIdTest() {
        Problem customerNotFoundProblem = template.exchange("/api/customers/0",
                HttpMethod.GET,
                null,
                Problem.class).getBody();

        assertEquals(404,customerNotFoundProblem.getStatus().getStatusCode());
    }

    @Test
    void updateCustomerWithInvalidIdTest() {
        Problem customerNotFoundProblem = template.exchange("/api/customers/0",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("John Doe", "Budapest", "Széchenyi István tér 4", "anothername@domain.com")),
                Problem.class).getBody();

        assertEquals(404,customerNotFoundProblem.getStatus().getStatusCode());
    }

    @Test
    void updateCustomerWithBlankAttributesTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("John Doe", "Budapest", "Széchenyi István tér 4", "anothername@domain.com"),
                CustomerDTO.class);

        Problem blankNameProblem = template.exchange("/api/customers/" + customerDTO.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("", "Budapest", "Széchenyi István tér 4", "anothername@domain.com")),
                Problem.class).getBody();
        Problem blankCityProblem = template.exchange("/api/customers/" + customerDTO.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("John Doe", "", "Széchenyi István tér 4", "anothername@domain.com")),
                Problem.class).getBody();
        Problem blankAddressProblem = template.exchange("/api/customers/" + customerDTO.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateCustomerCommand("John Doe", "Budapest", "", "anothername@domain.com")),
                Problem.class).getBody();

        assertEquals(400,blankNameProblem.getStatus().getStatusCode());
        assertEquals(400,blankCityProblem.getStatus().getStatusCode());
        assertEquals(400,blankAddressProblem.getStatus().getStatusCode());
    }

    @Test
    void deleteByIdTest() {
        CustomerDTO customerDTO = template.postForObject("/api/customers",
                new CreateCustomerCommand("John Doe", "Budapest", "Kossuth utca 2", "name@domain.com"),
                CustomerDTO.class);

        template.delete("/api/customers/" + customerDTO.getId());

        List<CustomerDTO> result = template.exchange("/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDTO>>() {
                }).getBody();

        assertEquals(0, result.size());
    }

    @Test
    void deleteByInvalidIdTest() {
        Problem problem = template.exchange("/api/customers/0",
                HttpMethod.DELETE,
                null,
                Problem.class).getBody();

        assertEquals(404, problem.getStatus().getStatusCode());
    }


}

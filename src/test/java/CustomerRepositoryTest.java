import br.com.krs.arqrefs.dataconnector.entity.Customer;
import br.com.krs.arqrefs.dataconnector.repository.CustomerRepository;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
@QuarkusTestResource(CustomResource.class)
public class CustomerRepositoryTest {

    @Inject
    CustomerRepository customerRepository;

    private Customer customer;


    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Test Customer");
        customer.setPhone("123456789");
        customer.setEmail("test@test.com");
        customer = customerRepository.getEntityManager().merge(customer);

    }


    @Test
    public void testFindByEmail() {
        Optional<Customer> found = customerRepository.findByEmail("test@test.com");
        assertTrue(found.isPresent());
        assertEquals("Test Customer", found.get().getName());

    }

    @Test
    public void testFindByIdOptional() {
        Optional<Customer> found = customerRepository.findByIdOptional(customer.getId());
        assertTrue(found.isPresent());
        assertEquals(customer.getId(), found.get().getId());
    }

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
    }
}

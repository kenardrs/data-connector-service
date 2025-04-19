
import br.com.krs.arqrefs.dataconnector.dto.CustomerRequest;
import br.com.krs.arqrefs.dataconnector.resource.CustomerResource;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(CustomerResource.class)
public class CustomerResourceTest {

    @Test
    @TestTransaction
    void testCustomerLifecycle() {
        // 1. Criação
        CustomerRequest request = new CustomerRequest(
                "Test User",
                "test@example.com",
                "+5511999999999"
        );

        String idStr = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(201)
                .header("Location", containsString("/api/customers/"))
                .body("name", is("Test User"))
                .extract()
                .path("id.toString()");

        UUID id = UUID.fromString(idStr);

        // 2. Busca
        given()
                .when()
                .get("/" + id)
                .then()
                .statusCode(200)
                .body("email", is("test@example.com"));

        // 3. Atualização
        request.setName("Updated Name");
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/" + id)
                .then()
                .statusCode(200)
                .body("name", is("Updated Name"));

        // 4. Exclusão
        given()
                .when()
                .delete("/" + id)
                .then()
                .statusCode(204);

        // 5. Verificação de exclusão
        given()
                .when()
                .get("/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    void testValidation() {
        // Teste todos os campos inválidos
        CustomerRequest invalid = new CustomerRequest("", "invalid", "abc");

        given()
                .contentType(ContentType.JSON)
                .body(invalid)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body(
                        containsString("Name is required"),
                        containsString("Email should be valid"),
                        containsString("Invalid phone number")
                );
    }
}
package br.com.krs.arqrefs.dataconnector.resource;

import br.com.krs.arqrefs.dataconnector.dto.CustomerRequest;
import br.com.krs.arqrefs.dataconnector.dto.CustomerResponse;
import br.com.krs.arqrefs.dataconnector.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customers", description = "Operations related to customers")
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @GET
    @Operation(summary = "Get all customers")
    @APIResponse(responseCode = "200", description = "List of all customers")
    public List<CustomerResponse> getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a customer by ID")
    @APIResponse(responseCode = "200", description = "The requested customer")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public CustomerResponse getCustomerById(@PathParam("id") UUID id) {
        return customerService.findCustomerById(id);
    }

    @POST
    @Operation(summary = "Create a new customer")
    @APIResponse(responseCode = "201", description = "Customer created successfully")
    @APIResponse(responseCode = "400", description = "Invalid customer data")
    public Response createCustomer(@Valid CustomerRequest customerRequest) {
        // Adicionada anotação @Valid
        CustomerResponse response = customerService.createCustomer(customerRequest);
        return Response.created(URI.create("/api/customers/" + response.getId()))
                .entity(response)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing customer")
    @APIResponse(responseCode = "200", description = "Customer updated successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public CustomerResponse updateCustomer(@PathParam("id") UUID id, CustomerRequest customerRequest) {
        return customerService.updateCustomer(id, customerRequest);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a customer")
    @APIResponse(responseCode = "204", description = "Customer deleted successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response deleteCustomer(@PathParam("id") UUID id) {
        customerService.deleteCustomer(id);
        return Response.noContent().build();
    }
}
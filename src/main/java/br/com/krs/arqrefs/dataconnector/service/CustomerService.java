package br.com.krs.arqrefs.dataconnector.service;

import br.com.krs.arqrefs.dataconnector.dto.CustomerRequest;
import br.com.krs.arqrefs.dataconnector.dto.CustomerResponse;
import br.com.krs.arqrefs.dataconnector.entity.Customer;
import br.com.krs.arqrefs.dataconnector.exception.CustomerNotFoundException;
import br.com.krs.arqrefs.dataconnector.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.listAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponse findCustomerById(UUID id) {
        return customerRepository.findByIdOptional(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente " + id + " não localizado"));
    }


    @Transactional
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setPhone(customerRequest.getPhone());
        customer.setEmail(customerRequest.getEmail());

        customerRepository.persist(customer);
        return mapToResponse(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        customer.setName(customerRequest.getName());
        customer.setPhone(customerRequest.getPhone());
        customer.setEmail(customerRequest.getEmail());

        return mapToResponse(customer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findByIdOptional(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .build();
    }


}

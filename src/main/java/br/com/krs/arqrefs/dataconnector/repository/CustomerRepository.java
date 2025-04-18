package br.com.krs.arqrefs.dataconnector.repository;

import br.com.krs.arqrefs.dataconnector.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    public Optional<Customer> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<Customer> findByIdOptional(UUID id) {
        return find("id", id).firstResultOptional();
    }
}

package com.bidomi.astrid.Repositories;

import com.bidomi.astrid.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByLastName(String lastName);
    List<User> findByFirstName(String firstName);
}

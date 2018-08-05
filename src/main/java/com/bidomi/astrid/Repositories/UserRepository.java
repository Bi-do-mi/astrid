package com.bidomi.astrid.Repositories;

import com.bidomi.astrid.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByLastName(String lastName);

    List<User> findByFirstName(String firstName);

    List<User> findByEmail(String email);

    User findById(long id);
}

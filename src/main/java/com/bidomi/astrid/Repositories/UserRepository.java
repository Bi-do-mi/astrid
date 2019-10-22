package com.bidomi.astrid.Repositories;

import com.bidomi.astrid.Model.User;
import com.bidomi.astrid.Repositories.SpatialQueries.UsersInPolygon;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>,
        UsersInPolygon<User> {

    Optional<User> findByUsername(String username);
    Optional<User> findByConfirmationToken(String confirmationToken);
    List<User> findAll();
}

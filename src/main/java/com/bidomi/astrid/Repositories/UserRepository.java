package com.bidomi.astrid.Repositories;

import com.bidomi.astrid.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByConfirmationToken(String confirmationToken);
    List<User> findAll();
}

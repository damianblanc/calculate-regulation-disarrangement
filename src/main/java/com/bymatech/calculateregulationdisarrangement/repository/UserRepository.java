package com.bymatech.calculateregulationdisarrangement.repository;

import com.bymatech.calculateregulationdisarrangement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);
}

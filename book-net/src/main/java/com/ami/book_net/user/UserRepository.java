package com.ami.book_net.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Optional<User> -> This forces whoever calls this method to explicitly handle the case where the user isn't found (e.g., userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"))). It completely prevents accidental NullPointerException crashes.

 * We didn't write any SQL code, so how does Spring know what to do? Spring Data JPA uses a feature called Query Derivation via Method Names.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}

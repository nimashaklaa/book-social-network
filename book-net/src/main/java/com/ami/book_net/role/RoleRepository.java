package com.ami.book_net.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * Extends JpaRepository to provide CRUD operations and additional query methods.
 *  Role-> The target Java Entity class this repository will manage.
 *  Integer-> The type of the primary key of the Role entity.
 * <p>
 *  JpaRepository, Spring automatically provides dozens of built-in database helper methods.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String role);
}

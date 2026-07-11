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
 *  Why not @Bean?
 *  You don't need @Bean or @Component here because extending JpaRepository is an explicit instruction to Spring. It's like flashing a VIP badge at the door—Spring instantly recognizes what the class is supposed to do, builds the database implementation for you under the hood, and registers it as a managed Bean without you having to type a single extra line of configuration.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String role);
}

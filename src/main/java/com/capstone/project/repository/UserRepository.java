package com.capstone.project.repository;

import com.capstone.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
//    Optional<User> findByUsername(String username);
//
//    Optional<User> findByEmail(String email);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserById(int id);

    @Query(value = "SELECT * FROM User WHERE username LIKE %:username% AND username != :except AND role != 'ROLE_ADMIN'", nativeQuery = true)
    List<User> findAllNameExcept(@Param("username") String username,
                                   @Param("except") String except);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByToken(String token);

    User findUserByToken(String token);

}

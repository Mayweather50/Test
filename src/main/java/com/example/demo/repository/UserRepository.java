package com.example.demo.repository;


import com.example.demo.entity.*;
import com.example.demo.enums.*;
import io.lettuce.core.dynamic.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // Найти пользователя по email
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);


    // Проверка на существование пользователя с определенным email
    boolean existsByEmail(String email);

    // Найти всех пользователей с определённой ролью (с пагинацией)
    //@Query("SELECT u FROM User u WHERE u.role = :role")
    //Page<User> findAllByRole(@Param("role") Role role, Pageable pageable);

}



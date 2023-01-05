package com.example.firstservice.repos;

import com.example.firstservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserName(String username);
    User findByFirstNameAndLastName(String firstName, String lastName);
    User findByEmailAddress(String email);
}

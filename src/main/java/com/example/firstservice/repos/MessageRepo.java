package com.example.firstservice.repos;

import com.example.firstservice.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
    Message findByTitle(String title);
}

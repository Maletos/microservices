package com.example.firstservice.repos;

import com.example.firstservice.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {
    Message findByTitle(String title);
}

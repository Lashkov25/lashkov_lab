package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.com.reactive.lashkov_lab.entity.Message;

public interface MessageRepository extends ReactiveCrudRepository<Message, Long> {
}

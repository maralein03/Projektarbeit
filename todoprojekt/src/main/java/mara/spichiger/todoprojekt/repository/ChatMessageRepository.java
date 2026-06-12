package mara.spichiger.todoprojekt.repository;

import mara.spichiger.todoprojekt.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByTodoIdOrderByCreatedAtAsc(Long todoId);
}

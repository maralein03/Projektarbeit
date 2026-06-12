package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.model.ChatMessage;
import mara.spichiger.todoprojekt.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    /**
     * Get all messages for a specific todo
     */
    @GetMapping("/todos/{todoId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long todoId) {
        List<ChatMessage> messages = chatMessageRepository.findByTodoIdOrderByCreatedAtAsc(todoId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Send a new message for a todo
     */
    @PostMapping("/todos/{todoId}/messages")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @PathVariable Long todoId,
            @RequestBody Map<String, String> payload) {
        
        String messageText = payload.get("message");
        if (messageText == null || messageText.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Message cannot be empty"));
        }

        // Get current username from JWT or authentication
        String sender = SecurityContextHolder.getContext().getAuthentication().getName();
        if (sender == null || sender.equals("anonymousUser")) {
            sender = "Unknown";
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setTodoId(todoId);
        chatMessage.setSender(sender);
        chatMessage.setMessage(messageText);
        chatMessage.setCreatedAt(LocalDateTime.now());

        ChatMessage saved = chatMessageRepository.save(chatMessage);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", saved
        ));
    }
}

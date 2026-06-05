package mara.spichiger.todoprojekt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import mara.spichiger.todoprojekt.model.Question;
import mara.spichiger.todoprojekt.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import mara.spichiger.todoprojekt.repository.TodoRepository;

@RestController
@RequestMapping("/api/todos/{id}/questions")
@Validated
@Tag(name = "Question Controller", description = "Verwaltet Fragen zu einem Todo (nur für Lernende mit ROLE_READ).")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TodoRepository todoRepository;

    @Operation(summary = "Frage hinzufügen", description = "Fügt dem Todo eine neue Frage hinzu. Erfordert ROLE_READ (Lernender).")
    @PostMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<Question> addQuestion(@PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id, @RequestBody Question question) {
        return todoRepository.findById(id).map(todo -> {
            question.setTodo(todo);
            Question saved = questionRepository.save(question);
            return ResponseEntity.status(201).body(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Fragen abrufen", description = "Listet alle Fragen zum angegebenen Todo auf. Erfordert ROLE_READ (Lernender).")
    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public List<Question> getQuestions(@PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id) {
        return questionRepository.findByTodoId(id);
    }
}
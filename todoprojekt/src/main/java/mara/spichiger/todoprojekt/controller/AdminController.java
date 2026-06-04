package mara.spichiger.todoprojekt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import mara.spichiger.todoprojekt.model.Todo;
import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@Validated
@Tag(name = "Admin Controller", description = "Erstellt und löscht Todo-Einträge (nur für Ausbilder mit ROLE_UPDATE).")
public class AdminController {

    @Autowired
    private TodoRepository todoRepository;

    @Operation(summary = "Todo erstellen", description = "Legt einen neuen Todo-Eintrag an. Nur für Ausbilder (ROLE_UPDATE).")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_UPDATE')")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        Todo saved = todoRepository.save(todo);
        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Todo löschen", description = "Löscht ein Todo nach ID, wenn es vorhanden ist. Nur für Ausbilder (ROLE_UPDATE).")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_UPDATE')")
    public ResponseEntity<Void> deleteTodo(@PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

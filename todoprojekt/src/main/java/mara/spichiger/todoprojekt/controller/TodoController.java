package mara.spichiger.todoprojekt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import mara.spichiger.todoprojekt.model.Todo;
import mara.spichiger.todoprojekt.model.Status;
import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@Validated
@Tag(name = "Todo Controller", description = "Liest und aktualisiert vorhandene Todo-Einträge.")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // Read all
    @Operation(summary = "Liste aller Todos", description = "Gibt alle Todo-Einträge zurück. Erfordert ROLE_READ (Lernender).")
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // Read single
    @Operation(summary = "Todo abrufen", description = "Gibt ein einzelnes Todo nach ID zurück, falls vorhanden. Erfordert ROLE_READ (Lernender).")
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @Operation(summary = "Todo aktualisieren", description = "Aktualisiert ein Todo, wenn es existiert. Erfordert ROLE_UPDATE (Ausbilder).")
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id, @Valid @RequestBody Todo todo) {
        return todoRepository.findById(id).map(existing -> {
            existing.setTitle(todo.getTitle());
            existing.setDescription(todo.getDescription());
            existing.setAssignedTo(todo.getAssignedTo());
            existing.setStatus(todo.getStatus());
            return ResponseEntity.ok(todoRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Partial update - Status only
    @Operation(summary = "Todo-Status aktualisieren", description = "Aktualisiert nur den Status eines Todos mittels PATCH. Alle Rollen können Status aktualisieren.")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTodoStatus(
            @PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id, 
            @RequestBody(required = true) java.util.Map<String, Object> updates) {
        
        java.util.Optional<Todo> todoOpt = todoRepository.findById(id);
        
        if (!todoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Todo existing = todoOpt.get();
        
        // Update status if provided
        if (updates.containsKey("status")) {
            String statusStr = (String) updates.get("status");
            try {
                existing.setStatus(Status.valueOf(statusStr));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid status value: " + statusStr);
            }
        }
        
        // Update other fields if provided
        if (updates.containsKey("title")) {
            existing.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            existing.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("assignedTo")) {
            existing.setAssignedTo((String) updates.get("assignedTo"));
        }
        
        Todo saved = todoRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

}
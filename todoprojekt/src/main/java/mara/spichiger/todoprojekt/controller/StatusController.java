package mara.spichiger.todoprojekt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/todos")
@Validated
@Tag(name = "Status Controller", description = "Ändert den Status eines Todos und nimmt Aufgaben an (nur für Lernende mit ROLE_READ).")
public class StatusController {

    @Autowired
    private TodoRepository todoRepository;

    // Aufgabe annehmen
    @Operation(summary = "Aufgabe annehmen", description = "Weist die Aufgabe einer Person zu und setzt den Status auf IN_PROGRESS. Erfordert ROLE_READ (Lernender).")
    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<Todo> acceptTodo(@PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id, @RequestParam String name) {
        return todoRepository.findById(id).map(todo -> {
            todo.setAssignedTo(name);
            todo.setStatus(Status.IN_PROGRESS);
            return ResponseEntity.ok(todoRepository.save(todo));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Status auf DONE oder QUESTION setzen
    @Operation(summary = "Status ändern", description = "Ändert den Status eines Todo-Eintrags (z.B. DONE oder QUESTION). Erfordert ROLE_READ (Lernender).")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<Todo> changeStatus(@PathVariable @NotNull(message = "id darf nicht null sein") @PositiveOrZero(message = "id darf nicht negativ sein") Long id, @RequestParam Status status) {
        return todoRepository.findById(id).map(todo -> {
            todo.setStatus(status);
            return ResponseEntity.ok(todoRepository.save(todo));
        }).orElse(ResponseEntity.notFound().build());
    }
}
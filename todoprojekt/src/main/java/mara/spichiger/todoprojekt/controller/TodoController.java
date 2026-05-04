package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.model.Todo;
import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // Kriterium: Liste aller Aufgaben abrufen
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // Kriterium: Neue Aufgabe erstellen (Nur Ausbilder/Update Rolle)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }
}
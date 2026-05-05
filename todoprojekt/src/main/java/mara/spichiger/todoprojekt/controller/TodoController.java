package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.model.Todo;
import mara.spichiger.todoprojekt.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    @PreAuthorize("hasRole('READ_TODO')")
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('READ_TODO')")
    public ResponseEntity<Todo> getTodo(@PathVariable Long id) {
        return todoService.getTodo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('UPDATE_TODO')")
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('UPDATE_TODO')")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

}
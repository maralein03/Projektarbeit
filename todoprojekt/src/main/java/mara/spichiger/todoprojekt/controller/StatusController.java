package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.model.Status;
import mara.spichiger.todoprojekt.model.Todo;
import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class StatusController {

    @Autowired
    private TodoRepository todoRepository;

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasRole('READ_TODO')")
    public Todo acceptTodo(@PathVariable Long id, @RequestParam String name) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setAssignedTo(name);
        todo.setStatus(Status.IN_PROGRESS);
        return todoRepository.save(todo);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('READ_TODO')")
    public Todo changeStatus(@PathVariable Long id, @RequestParam Status status) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setStatus(status);
        return todoRepository.save(todo);
    }
}
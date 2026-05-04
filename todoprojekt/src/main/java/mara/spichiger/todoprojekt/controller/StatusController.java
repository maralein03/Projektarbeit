package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.model.Todo;
import mara.spichiger.todoprojekt.model.Status;
import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class StatusController {

    @Autowired
    private TodoRepository todoRepository;

    // Kriterium: Aufgabe annehmen
    @PatchMapping("/{id}/accept")
    public Todo acceptTodo(@PathVariable Long id, @RequestParam String name) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setAssignedTo(name);
        todo.setStatus(Status.IN_PROGRESS);
        return todoRepository.save(todo);
    }

    // Kriterium: Status auf DONE oder QUESTION setzen
    @PatchMapping("/{id}/status")
    public Todo changeStatus(@PathVariable Long id, @RequestParam Status status) {
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setStatus(status);
        return todoRepository.save(todo);
    }
}
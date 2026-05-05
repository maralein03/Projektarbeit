package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructors/todos")
public class AusbilderController {

    @Autowired
    private TodoRepository todoRepository;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('UPDATE_TODO')")
    public void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}
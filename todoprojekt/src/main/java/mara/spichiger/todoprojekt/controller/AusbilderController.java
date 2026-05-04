package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructors/todos")
public class AusbilderController {

    @Autowired
    private TodoRepository todoRepository;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public void deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}
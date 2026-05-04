package mara.spichiger.todoprojekt.controller;

import mara.spichiger.todoprojekt.model.Question;
import mara.spichiger.todoprojekt.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import mara.spichiger.todoprojekt.repository.TodoRepository;

@RestController
@RequestMapping("/api/todos/{id}/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TodoRepository todoRepository;

    @PostMapping
    public Question addQuestion(@PathVariable Long id, @RequestBody Question question) {
        return todoRepository.findById(id).map(todo -> {
            question.setTodo(todo);
            return questionRepository.save(question);
        }).orElseThrow();
    }

    @GetMapping
    public List<Question> getQuestions(@PathVariable Long id) {
        return questionRepository.findAll();
    }
}
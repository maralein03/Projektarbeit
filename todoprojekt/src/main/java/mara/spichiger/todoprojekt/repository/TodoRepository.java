package mara.spichiger.todoprojekt.repository;

import mara.spichiger.todoprojekt.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // Hier erbt Spring Boot automatisch alle Methoden wie save(), findAll(), deleteById()
}
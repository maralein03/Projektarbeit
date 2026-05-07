package mara.spichiger.todoprojekt;

import mara.spichiger.todoprojekt.model.Status;
import mara.spichiger.todoprojekt.model.Todo;
import mara.spichiger.todoprojekt.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DBTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void testDatabaseConnection() {

        // arrange
        Todo todo = new Todo();
        todo.setTitle("DB-Test Todo");
        todo.setDescription("Test-Beschreibung für Datenbankverbindung");
        todo.setStatus(Status.OPEN);
        // act
        Todo saved = todoRepository.save(todo);

        // assert
        assertNotNull(saved.getId());
    }
}

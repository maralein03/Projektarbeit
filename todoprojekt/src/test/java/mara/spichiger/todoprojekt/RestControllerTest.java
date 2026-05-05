package mara.spichiger.todoprojekt;

import mara.spichiger.todoprojekt.controller.TodoController;
import mara.spichiger.todoprojekt.service.TodoService;
import mara.spichiger.todoprojekt.repository.TodoRepository; // NEU
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(TodoController.class)
public class RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoRepository todoRepository;

    @Test
    @WithMockUser(roles = "UPDATE")
    void testGetAllTodos() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "UPDATE")
    void testCreateTodo() throws Exception {
        String todoJson = "{\"firstName\":\"Mara\",\"lastName\":\"Spichiger\",\"status\":\"OPEN\"}";

        mockMvc.perform(post("/api/todos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(status().isOk());
    }
}
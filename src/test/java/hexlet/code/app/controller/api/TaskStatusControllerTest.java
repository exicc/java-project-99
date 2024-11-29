package hexlet.code.app.controller.api;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.request
        .SecurityMockMvcRequestPostProcessors
        .JwtRequestPostProcessor;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository repository;
    private JwtRequestPostProcessor token;

    private String lastTaskStatusID;

    @BeforeEach
    public void setup() throws Exception {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        String dto = """
                {
                    "name": "Mike",
                    "slug": "Logan"
                }""";

        mockMvc.perform(post("/api/task_statuses")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andDo(print());
        Optional<TaskStatus> lastTaskStatus = repository.getBySlug("Logan");
        lastTaskStatusID = lastTaskStatus.get().getId().toString();
    }
    @AfterEach
    public void postSetup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Test create")
    public void createTest() throws Exception {
        String dto = """
                {
                    "name": "George",
                    "slug": "Princeton"
                }""";

        mockMvc.perform(post("/api/task_statuses")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("Test get list")
    public void getListTest() throws Exception {
        mockMvc.perform(get("/api/task_statuses")
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test get one")
    public void getOneTest() throws Exception {
        String id = lastTaskStatusID;

        mockMvc.perform(get("/api/task_statuses/{id}", id)
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test update")
    public void updateTest() throws Exception {
        String taskData = """
                {
                    "name": "ewq",
                    "slug": "ewq"
                }""";
        String id = lastTaskStatusID;

        mockMvc.perform(put("/api/task_statuses/{id}", id)
                        .content(taskData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test delete")
    public void deleteTest() throws Exception {
        String id = lastTaskStatusID;

        mockMvc.perform(delete("/api/task_statuses/{id}", id)
                        .with(token))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}

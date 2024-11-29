package hexlet.code.app.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    private JwtRequestPostProcessor token;

    private String lastTaskId;
    private String lastUserId;
    private String lastTaskStatusId;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @Transactional
    public void setup() throws Exception {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        String email = "hexlet@example.com";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email "
                        + email + " not found. | Test suite"));

        String slug = "draft";
        TaskStatus taskStatus = taskStatusRepository.getBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Task status with slug "
                        + slug + " not found. | Test suite"));


        TaskCreateDTO newTask = new TaskCreateDTO();
        newTask.setIndex(123);
        newTask.setTitle("Test Task");
        newTask.setContent("This is a test task");
        newTask.setTaskStatusId(taskStatus.getId());
        newTask.setAssigneeId(user.getId());


        String taskJson = objectMapper.writeValueAsString(newTask);
        System.out.println(taskJson);

        mockMvc.perform(post("/api/tasks")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andDo(print());
        Task lastTask = taskRepository.findByIndex(newTask.getIndex());
        lastTaskId = lastTask.getId().toString();
        lastUserId = user.getId().toString();
        lastTaskStatusId = taskStatus.getId().toString();
    }
    @AfterEach
    public void postSetup() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Test create")
    public void createTest() throws Exception {
        String dto = String.format("""
                {
                   "name": "Task name",
                   "index": 10,
                   "description": "Task description",
                   "taskStatusId": %s,
                   "assigneeId": %s
                 }""", lastTaskStatusId, lastUserId);

        mockMvc.perform(post("/api/tasks")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("Test get list")
    public void getListTest() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test get one")
    public void getOneTest() throws Exception {
        String id = lastTaskId;

        mockMvc.perform(get("/api/tasks/{id}", id)
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test update")
    public void updateTest() throws Exception {
        String taskData = """
                {
                   "name": "New task name",
                   "description": "New task description"
                 }""";
        String id = lastTaskId;

        mockMvc.perform(put("/api/tasks/{id}", id)
                        .content(taskData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test delete")
    public void deleteTest() throws Exception {
        String id = lastTaskId;

        mockMvc.perform(delete("/api/tasks/{id}", id)
                        .with(token))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}

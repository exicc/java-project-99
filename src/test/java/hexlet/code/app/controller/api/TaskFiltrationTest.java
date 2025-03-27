package hexlet.code.app.controller.api;

import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskFiltrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private Long userId;
    private Long labelId;


    @BeforeEach
    public void setup() {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        String email = "hexlet@example.com";
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPasswordDigest("password");
                    return userRepository.save(newUser);
                });
        userId = user.getId();

        String slug = "draft";
        TaskStatus taskStatus = taskStatusRepository.getBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Task status with slug "
                        + slug + " not found."));


        Label label = new Label();
        label.setName("label");
        labelRepository.save(label);
        labelId = label.getId();

        Task matchingTask = new Task();
        matchingTask.setName("Task 1");
        matchingTask.setDescription("A matching task");
        matchingTask.setAssignee(user);
        matchingTask.setTaskStatus(taskStatus);
        matchingTask.addLabel(label);
        System.out.println(taskRepository.save(matchingTask));

        Task nonMatchingTask = new Task();
        nonMatchingTask.setName("Task 2");
        nonMatchingTask.setDescription("Another task");
        nonMatchingTask.setTaskStatus(taskStatus);
        System.out.println(taskRepository.save(nonMatchingTask));
    }
    @AfterEach
    public void postSetup() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get list with filters")
    public void getListFilters() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("titleCont", "Task");
        params.add("assigneeId", userId.toString());
        params.add("status", "draft");
        params.add("labelId", labelId.toString());

        mockMvc.perform(get("/api/tasks")
                        .with(token)
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Task 1"))
                .andExpect(jsonPath("$.content[?(@.title == 'Another task')]").doesNotExist())
                .andExpect(jsonPath("$.content[0].assignee_id").value(userId))
                .andExpect(jsonPath("$.content[0].status").value("draft"))
                .andExpect(jsonPath("$.content[0].label_ids[0]").value(labelId))
                .andDo(print());
    }
}

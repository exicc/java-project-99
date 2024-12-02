package hexlet.code.app.controller.api;

import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository repository;
    private JwtRequestPostProcessor token;

    private String lastLabelID;

    @BeforeEach
    public void setup() throws Exception {
        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        String dto = """
                {
                    "name": "Work"
                }""";

        mockMvc.perform(post("/api/labels")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andDo(print());
        System.out.println("TEST DATA");
        repository.findAll().forEach(label -> System.out.println(label.getName()));

        Optional<Label> lastLabel = repository.getByName("Work");
        lastLabelID = lastLabel.get().getId().toString();
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
                    "name": "Abandoned"
                }""";

        mockMvc.perform(post("/api/labels")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Abandoned"))
                .andDo(print());
    }

    @Test
    @DisplayName("Test get list")
    public void getListTest() throws Exception {
        mockMvc.perform(get("/api/labels")
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test get one")
    public void getOneTest() throws Exception {
        String id = lastLabelID;

        mockMvc.perform(get("/api/labels/{id}", id)
                        .with(token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Test update")
    public void updateTest() throws Exception {
        String taskData = """
                {
                    "name": "New name"
                }""";
        String id = lastLabelID;

        mockMvc.perform(put("/api/labels/{id}", id)
                        .content(taskData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New name"))
                .andDo(print());
    }

    @Test
    @DisplayName("Test delete")
    public void deleteTest() throws Exception {
        String id = lastLabelID;

        mockMvc.perform(delete("/api/labels/{id}", id)
                        .with(token))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}

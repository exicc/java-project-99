package hexlet.code.app.component;

import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) {

        createStartUsers();
        createStartTaskStatuses();
        createStartLabels();
    }
    private void createStartUsers() {
        var email = "hexlet@example.com";
        var user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            taskRepository.deleteAllByAssigneeId(user.get().getId());
            userRepository.deleteAllByEmailSQL(email);
        }
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);
    }
    private void createStartTaskStatuses() {
        List<String> initialStatuses = List.of("DRAFT", "TO_REVIEW", "TO_BE_FIXED", "TO_PUBLISH", "PUBLISHED");
        for (String status : initialStatuses) {
            if (!taskStatusRepository.existsByName(status)) {
                TaskStatus newStatus = new TaskStatus();
                newStatus.setName(status);
                newStatus.setSlug(status.toLowerCase());
                taskStatusRepository.save(newStatus);
            }
        }
    }
    private void createStartLabels() {
        List<String> initialLabels = List.of("feature", "bug");
        for (String label: initialLabels) {
            if (!labelRepository.existsByName(label)) {
                Label newLabel = new Label();
                newLabel.setName(label);
                labelRepository.save(newLabel);
            }
        }
    }
}

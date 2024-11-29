package hexlet.code.app.dto.taskStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link hexlet.code.app.model.TaskStatus}
 */
@AllArgsConstructor
@Getter
public class TaskStatusCreateDTO {

    private final String name;
    private final String slug;
}

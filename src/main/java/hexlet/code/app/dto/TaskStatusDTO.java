package hexlet.code.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * DTO for {@link hexlet.code.app.model.TaskStatus}
 */
@AllArgsConstructor
@Getter
public class TaskStatusDTO {
    private final Long id;
    @NotNull
    @Size(message = "Name must have at least 1 character", min = 1)
    private final String name;
    @NotNull
    @Size(message = "Slug must have at least 1 character", min = 1)
    private final String slug;
    private final LocalDateTime created;
}

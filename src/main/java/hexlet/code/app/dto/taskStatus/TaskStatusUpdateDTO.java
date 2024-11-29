package hexlet.code.app.dto.taskStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openapitools.jackson.nullable.JsonNullable;


@AllArgsConstructor
@Getter
public class TaskStatusUpdateDTO {

    @NotNull
    private final JsonNullable<String> name;

    @NotNull
    private final JsonNullable<String> slug;

}

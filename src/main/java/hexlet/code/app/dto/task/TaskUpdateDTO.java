package hexlet.code.app.dto.task;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<Integer> index;
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<Long> taskStatusId;
    private JsonNullable<Long> assigneeId;
}
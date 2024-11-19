package hexlet.code.app.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    private Integer index;
    private String title;
    private String content;
    private Long taskStatusId;
    private Long assigneeId;
}

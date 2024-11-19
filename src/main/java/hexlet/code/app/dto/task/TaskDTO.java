package hexlet.code.app.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private String createdAt;
    private Long assigneeId;
    private String title;
    private String content;
    private String status;
}

package hexlet.code.app.dto.label;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class LabelDTO {
    private final Long id;
    @Size(min = 3, max = 1000)
    private final String name;
    private final LocalDateTime createdAt;
}

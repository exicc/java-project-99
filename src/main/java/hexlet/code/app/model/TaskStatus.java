package hexlet.code.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TaskStatus implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, message = "Name must have at least 1 character")
    @Column(unique = true)
    private String name;

    @NotNull
    @Size(min = 1, message = "Slug must have at least 1 character")
    @Column(unique = true)
    private String slug;

    @CreatedDate
    private LocalDateTime created;
}

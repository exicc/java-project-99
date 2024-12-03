package hexlet.code.app.model;

import hexlet.code.app.repository.LabelRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
public class Task implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task name is mandatory")
    @Size(min = 1, message = "Task name must have at least 1 character")
    private String name;

    @Min(value = 0, message = "Index must be a positive number")
    private Integer index;

    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_status_id", nullable = false)
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToMany
    @JoinTable(
            name = "task_labels",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels = new HashSet<>();

    public void setLabelIds(Set<Long> labelIds, LabelRepository labelRepository) {
        if (labelIds == null || labelIds.isEmpty()) {
            this.labels.clear();
            return;
        }

        this.labels = new HashSet<>(labelRepository.findAllById(labelIds));
    }

    public void addLabel(Label label) {
        this.labels.add(label);
    }

    @CreatedDate
    private LocalDateTime createdAt;
}

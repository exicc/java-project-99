package hexlet.code.app.repository;

import hexlet.code.app.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    boolean existsByName(String name);
    Optional<TaskStatus> getBySlug(String slug);
}

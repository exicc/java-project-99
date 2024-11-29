package hexlet.code.app.repository;

import hexlet.code.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByAssigneeId(Long userID);
    boolean existsByTaskStatusId(Long statusID);
    Task findByName(String name);
    Task findByIndex(Integer index);
}

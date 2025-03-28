package hexlet.code.app.repository;

import hexlet.code.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    boolean existsByAssigneeId(Long userID);
    boolean existsByTaskStatusId(Long statusID);
    Task findByName(String name);
    Task findByIndex(Integer index);
    void deleteAllByAssigneeId(Long userID);
}

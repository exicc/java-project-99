package hexlet.code.app.repository;

import hexlet.code.app.model.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.assignee.id = :userId")
    void deleteAllByAssigneeId(@Param("userId") Long userId);
    boolean existsByAssigneeId(Long userID);
    boolean existsByTaskStatusId(Long statusID);
    Task findByName(String name);
    Task findByIndex(Integer index);
}

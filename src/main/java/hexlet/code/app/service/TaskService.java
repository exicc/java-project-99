package hexlet.code.app.service;

import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private final TaskStatusRepository taskStatusRepository;

    private final UserRepository userRepository;

    public Page<TaskDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(taskMapper::toDTO);
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.toDTO(task);
    }

    public TaskDTO createTask(TaskCreateDTO taskCreateDTO) {
        Task task = taskMapper.toEntity(taskCreateDTO);
        TaskStatus taskStatus = taskStatusRepository.findById(taskCreateDTO.getTaskStatusId())
                .orElseThrow(() -> new EntityNotFoundException("TaskStatus with id "
                        + taskCreateDTO.getTaskStatusId() + " not found"));

        User assignee = userRepository.findById(taskCreateDTO.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("User with id "
                        + taskCreateDTO.getAssigneeId() + " not found"));

        task.setTaskStatus(taskStatus);
        task.setAssignee(assignee);
        task = taskRepository.save(task);
        System.out.println(task);
        return taskMapper.toDTO(task);
    }

    public TaskDTO updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        taskMapper.partialUpdate(taskUpdateDTO, task);
        task = taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }
}


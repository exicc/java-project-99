package hexlet.code.app.service;

import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    private final TaskStatusRepository taskStatusRepository;

    private final UserRepository userRepository;

    private final LabelRepository labelRepository;

    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
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

        if (taskCreateDTO.getLabelIds() != null) {
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(taskCreateDTO.getLabelIds()));
            task.setLabels(labels);
        }

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

        if (taskUpdateDTO.getLabelIds() != null && taskUpdateDTO.getLabelIds().isPresent()) {
            Set<Label> labels = new HashSet<>(labelRepository.findAllById(taskUpdateDTO.getLabelIds().get()));
            task.setLabels(labels);
        }

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

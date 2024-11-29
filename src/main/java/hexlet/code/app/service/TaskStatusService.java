package hexlet.code.app.service;

import hexlet.code.app.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusDTO;
import hexlet.code.app.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        List<TaskStatus> taskStatuses = taskStatusRepository.findAll();
        return taskStatuses.stream()
                .map(taskStatusMapper::toDto)
                .toList();
    }

    public TaskStatusDTO getById(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
        return taskStatusMapper.toDto(status);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        TaskStatus taskStatus = taskStatusMapper.toEntity(dto);
        TaskStatus savedTaskStatus = taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(savedTaskStatus);
    }

    @Transactional
    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto) {
        TaskStatus existingStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));

        taskStatusMapper.partialUpdate(dto, existingStatus);
        TaskStatus updatedStatus = taskStatusRepository.save(existingStatus);
        return taskStatusMapper.toDto(updatedStatus);
    }

    @Transactional
    public void delete(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));

        boolean hasTasks = taskRepository.existsByTaskStatusId(id);
        if (hasTasks) {
            throw new RuntimeException("Cannot delete TaskStatus with id " + id + " as it is linked to existing tasks");
        }

        taskStatusRepository.delete(status);
    }
}

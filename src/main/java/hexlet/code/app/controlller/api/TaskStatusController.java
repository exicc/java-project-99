package hexlet.code.app.controlller.api;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusRepository taskStatusRepository;

    private final TaskStatusMapper taskStatusMapper;


    @GetMapping("/task_statuses")
    public Page<TaskStatusDTO> getList(@ParameterObject Pageable pageable) {
        Page<TaskStatus> taskStatuses = taskStatusRepository.findAll(pageable);
        return taskStatuses.map(taskStatusMapper::toDto);
    }

    @GetMapping("task_statuses/{id}")
    public TaskStatusDTO getOne(@PathVariable Long id) {
        Optional<TaskStatus> taskStatusOptional = taskStatusRepository.findById(id);
        return taskStatusMapper.toDto(taskStatusOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with id `%s` not found".formatted(id))));
    }

    @PostMapping("/task_statuses")
    public TaskStatusDTO create(@RequestBody @Valid TaskStatusCreateDTO dto) {
        TaskStatus taskStatus = taskStatusMapper.toEntity(dto);
        TaskStatus resultTaskStatus = taskStatusRepository.save(taskStatus);
        return taskStatusMapper.toDto(resultTaskStatus);
    }

    @PutMapping("/task_statuses/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO update(@Valid @RequestBody TaskStatusUpdateDTO taskData, @PathVariable Long id) {
        var task = taskStatusRepository.findBySlug(String.valueOf(taskData.getSlug()))
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

        taskStatusMapper.partialUpdate(taskData, task);
        taskStatusRepository.save(task);
        return taskStatusMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    public TaskStatusDTO delete(@PathVariable Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElse(null);
        if (taskStatus != null) {
            taskStatusRepository.delete(taskStatus);
        }
        return taskStatusMapper.toDto(taskStatus);
    }
}

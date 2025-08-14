package com.amin.taskmanagement.service;

import com.amin.taskmanagement.exception.DuplicateTaskException;
import com.amin.taskmanagement.exception.TaskNotFoundException;
import com.amin.taskmanagement.model.Task;
import com.amin.taskmanagement.model.dto.TaskRequest;
import com.amin.taskmanagement.model.dto.TaskResponse;
import com.amin.taskmanagement.model.mapper.TaskMapper;
import com.amin.taskmanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repo;
    private final TaskMapper mapper;

    public List<TaskResponse> getAllTasks() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }

    public TaskResponse getTask(Long id) {
        return mapper.toResponse(repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id)));
    }

    public void deleteTask(Long id) {
        if (!repo.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repo.deleteById(id);
    }

    public TaskResponse createTask(TaskRequest request) {
        if (repo.existsByTitle(request.getTitle())) {
            throw new DuplicateTaskException(request.getTitle());
        }
        Task entity = mapper.toEntity(request);
        return mapper.toResponse(repo.save(entity));
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task existingTask = repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        if (isDuplicateTitle(request.getTitle(), existingTask.getId())) {
            throw new DuplicateTaskException(request.getTitle());
        }

        mapper.updateEntityFromDto(request, existingTask);
        Task updatedTask = repo.save(existingTask);
        return mapper.toResponse(updatedTask);
    }

    private boolean isDuplicateTitle(String title, Long currentTaskId) {
        return repo.findAllByTitle(title).stream().anyMatch(task -> !task.getId().equals(currentTaskId));
    }

}

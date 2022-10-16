package io.github.mmm.controller;

import io.github.mmm.model.Task;
import io.github.mmm.repo.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toSave) {
        Task result = repository.save(toSave);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}

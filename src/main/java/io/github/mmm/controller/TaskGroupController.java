package io.github.mmm.controller;

import io.github.mmm.logic.TaskGroupService;
import io.github.mmm.model.Task;
import io.github.mmm.model.projection.GroupReadModel;
import io.github.mmm.model.projection.GroupWriteModel;
import io.github.mmm.repo.TaskRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
@AllArgsConstructor
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    @PostMapping
    ResponseEntity<GroupReadModel> createTaskGroup(@RequestBody @Valid GroupWriteModel taskGroup) {
        GroupReadModel group = taskGroupService.createGroup(taskGroup);
        return ResponseEntity.created(URI.create("/" + group.getId())).body(group);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readReadGroups() {
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @PatchMapping("/edit/{groupId}")
    ResponseEntity<Void> toggleGroup(@PathVariable int groupId) {
        taskGroupService.toggleGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/groups/{id}/tasks")
    ResponseEntity<List<Task>> getAllTaskFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

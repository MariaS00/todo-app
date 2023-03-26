package io.github.mmm.controller;

import io.github.mmm.logic.TaskGroupService;
import io.github.mmm.model.Task;
import io.github.mmm.model.projection.*;
import io.github.mmm.repo.TaskRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
@IllegalExceptionProcessing
@AllArgsConstructor
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute("group") @Valid GroupWriteModel current,
                    BindingResult bindingResult,
                    Model model) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }
        taskGroupService.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę!");
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createTaskGroup(@RequestBody @Valid GroupWriteModel taskGroup) {
        GroupReadModel group = taskGroupService.createGroup(taskGroup);
        return ResponseEntity.created(URI.create("/" + group.getId())).body(group);
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readReadGroups() {
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @ResponseBody
    @PatchMapping(value = "/edit/{groupId}")
    ResponseEntity<Void> toggleGroup(@PathVariable int groupId) {
        taskGroupService.toggleGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @GetMapping(value = "/groups/{id}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> getAllTaskFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @ModelAttribute("groups")
    private List<GroupReadModel> getGroups() {
        return taskGroupService.readAll();
    }
}

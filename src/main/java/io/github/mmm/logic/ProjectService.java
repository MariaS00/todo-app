package io.github.mmm.logic;

import io.github.mmm.TaskConfigurationProperties;
import io.github.mmm.model.Project;
import io.github.mmm.model.projection.GroupReadModel;
import io.github.mmm.model.projection.GroupTaskWriteModel;
import io.github.mmm.model.projection.GroupWriteModel;
import io.github.mmm.repo.ProjectRepository;
import io.github.mmm.repo.TaskGroupRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;
    private final TaskGroupRepository groupRepository;
    private final TaskConfigurationProperties config;
    private final TaskGroupService taskGroupService;

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project saveProject(Project source) {
        return repository.save(source);
    }


    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && groupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed!");
        }

        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(projectStep -> {
                                var task = new GroupTaskWriteModel();
                                task.setDescription(projectStep.getDescription());
                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                return task;
                            })
                            .collect(Collectors.toSet()));
                    return taskGroupService.createGroup(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with id: " + projectId + " not found"));
    }

}

package io.github.mmm.logic;

import io.github.mmm.TaskConfigurationProperties;
import io.github.mmm.model.Project;
import io.github.mmm.model.Task;
import io.github.mmm.model.TaskGroup;
import io.github.mmm.model.projection.GroupReadModel;
import io.github.mmm.repo.ProjectRepository;
import io.github.mmm.repo.TaskGroupRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final TaskGroupRepository groupRepository;
    private final TaskConfigurationProperties config;

    public ProjectService(ProjectRepository repository, TaskGroupRepository groupRepository, TaskConfigurationProperties config) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.config = config;
    }

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
        TaskGroup taskGroup = repository.findById(projectId).map(project -> {
            var result = new TaskGroup();
            result.setDescription(project.getDescription());
            result.setTasks(project.getSteps().stream()
                    .map(projectStep -> new Task(projectStep.getDescription(), deadline.plusDays(projectStep.getDaysToDeadline())))
                    .collect(Collectors.toSet()));
            result.setProject(project);
            return groupRepository.save(result);
        }).orElseThrow(() -> new IllegalArgumentException("Project with id: " + projectId + " not found"));

        return new GroupReadModel(taskGroup);
    }

}

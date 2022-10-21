package io.github.mmm.logic;

import io.github.mmm.TaskConfigurationProperties;
import io.github.mmm.model.Project;
import io.github.mmm.model.ProjectStep;
import io.github.mmm.model.TaskGroup;
import io.github.mmm.model.projection.GroupReadModel;
import io.github.mmm.repo.ProjectRepository;
import io.github.mmm.repo.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void noMultipleGroupsConfigAndUndoneGroupExists_throwsIllegalStateException() {
        TaskGroupRepository groupRepositoryMock = groupRepositoryReturning(true);

        TaskConfigurationProperties configMock = configurationReturning(false);

        ProjectService projectService = new ProjectService(null, groupRepositoryMock, configMock);

        var exception = catchThrowable(() -> projectService.createGroup(4, LocalDateTime.now()));

        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessage("Only one undone group from project is allowed!");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when configuration ok and no project for given id")
    void configurationOkAndNoProjects_throwsIllegalArgumentException() {
        TaskConfigurationProperties configMock = configurationReturning(true);

        var repositoryMock = mock(ProjectRepository.class);
        when(repositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        ProjectService projectService = new ProjectService(repositoryMock, null, configMock);
        int projectId = 4;

        var exception = catchThrowable(() -> projectService.createGroup(projectId, LocalDateTime.now()));

        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Project with id: " + projectId + " not found");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when configuration to allow just 1 group and no groups and project for given id")
    void noMultipleGroupsConfigAndNoUndoneGroupExistsNoProject_throwsIllegalArgumentException() {
        var repositoryMock = mock(ProjectRepository.class);
        when(repositoryMock.findById(anyInt())).thenReturn(Optional.empty());

        TaskGroupRepository groupRepositoryMock = groupRepositoryReturning(false);

        TaskConfigurationProperties configMock = configurationReturning(true);


        ProjectService projectService = new ProjectService(repositoryMock, groupRepositoryMock, configMock);
        int projectId = 4;

        var exception = catchThrowable(() -> projectService.createGroup(projectId, LocalDateTime.now()));

        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("Project with id: " + projectId + " not found");
    }


    @Test
    @DisplayName("Should create a new group from project")
    void createGroup() {
        var today = LocalDate.now().atStartOfDay();
        var project = projectWith("foo", Set.of(-1, -2));
        var repositoryMock = mock(ProjectRepository.class);
        when(repositoryMock.findById(anyInt())).thenReturn(Optional.of(project));

        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        int countBeforeCall = inMemoryGroupRepo.count();

        TaskConfigurationProperties configMock = configurationReturning(true);

        ProjectService projectService = new ProjectService(repositoryMock, inMemoryGroupRepo, configMock);

        GroupReadModel result = projectService.createGroup(1, today);

        assertThat(result.getDescription()).isEqualTo("foo");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("xd"));

        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private static TaskConfigurationProperties configurationReturning(boolean result) {
        var templateMock = mock(TaskConfigurationProperties.Template.class);
        when(templateMock.isAllowMultipleTasks()).thenReturn(result);

        var configMock = mock(TaskConfigurationProperties.class);
        when(configMock.getTemplate()).thenReturn(templateMock);

        return configMock;
    }

    private static TaskGroupRepository groupRepositoryReturning(boolean value) {
        var groupRepositoryMock = mock(TaskGroupRepository.class);
        when(groupRepositoryMock.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(value);
        return groupRepositoryMock;
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("xd");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                map.put(++index, entity);
                entity.setId(index);
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }
}

package io.github.mmm.logic;

import io.github.mmm.model.TaskGroup;
import io.github.mmm.repo.TaskGroupRepository;
import io.github.mmm.repo.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {


    @Test
    @DisplayName("should throw when undone tasks")
    void groupWithUndoneTasksExist_throwsIllegalStateException() {
        var taskRepoMock = mock(TaskRepository.class);
        when(taskRepoMock.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);

        TaskGroupService taskGroupService = new TaskGroupService(null, taskRepoMock);

        var exception = catchThrowable(() -> taskGroupService.toggleGroup(25));
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessage("Group has undone tasks. Done all the tasks first!");
    }

    @Test
    @DisplayName("should throw when no group")
    void groupWithUndoneTasksExist_throwsIllegalArgumentException() {
        var groupId = 258;
        var taskRepoMock = mock(TaskRepository.class);
        when(taskRepoMock.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        var taskGroupRepoMock = mock(TaskGroupRepository.class);
        when(taskGroupRepoMock.findById(groupId)).thenReturn(Optional.empty());

        TaskGroupService taskGroupService = new TaskGroupService(taskGroupRepoMock, taskRepoMock);

        var exception = catchThrowable(() -> taskGroupService.toggleGroup(groupId));
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("TaskGroup with id: " + groupId + " not found!");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup() {
        var groupId = 258;
        var taskRepoMock = mock(TaskRepository.class);
        when(taskRepoMock.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);

        var taskGroup = new TaskGroup();
        var beforeToggle = taskGroup.isDone();

        var taskGroupRepoMock = mock(TaskGroupRepository.class);
        when(taskGroupRepoMock.findById(groupId)).thenReturn(Optional.of(taskGroup));

        TaskGroupService taskGroupService = new TaskGroupService(taskGroupRepoMock, taskRepoMock);
        taskGroupService.toggleGroup(groupId);

        assertThat(taskGroup.isDone()).isEqualTo(!beforeToggle);
    }
}
package io.github.mmm.logic;

import io.github.mmm.model.TaskGroup;
import io.github.mmm.model.projection.GroupReadModel;
import io.github.mmm.model.projection.GroupWriteModel;
import io.github.mmm.repo.TaskGroupRepository;
import io.github.mmm.repo.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {

    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return new GroupReadModel(repository.save(source.toGroup()));
    }


    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first!");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with id: " + groupId + " not found!"));
        result.setDone(!result.isDone());
        repository.save(result);
    }

}

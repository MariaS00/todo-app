package io.github.mmm.repo;

import io.github.mmm.model.TaskGroup;

import java.util.List;
import java.util.Optional;

public interface TaskGroupRepository {

    List<TaskGroup> findAll();

    Optional<TaskGroup> findById(Integer id);

    TaskGroup save(TaskGroup entity);

    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

    boolean existsByDescription(String description);
}

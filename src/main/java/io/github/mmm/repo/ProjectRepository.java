package io.github.mmm.repo;

import io.github.mmm.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    List<Project> findAll();

    Optional<Project> findById(Integer id);

    Project save(Project entity);
}

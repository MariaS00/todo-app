package io.github.mmm.adapter;

import io.github.mmm.model.Project;
import io.github.mmm.repo.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();

    @Override
    Optional<Project> findById(Integer id);

    @Override
    Project save(Project entity);
}

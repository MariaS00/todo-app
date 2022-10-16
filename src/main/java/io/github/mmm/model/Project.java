package io.github.mmm.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;
}

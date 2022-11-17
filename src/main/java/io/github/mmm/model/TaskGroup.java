package io.github.mmm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "task_groups")
@Getter
@Setter
@NoArgsConstructor
public class TaskGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    private boolean done;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public TaskGroup(String description, boolean done, Set<Task> tasks, Project project) {
        this.description = description;
        this.done = done;
        this.tasks = tasks;
        this.project = project;
    }
}

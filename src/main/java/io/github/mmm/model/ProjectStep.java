package io.github.mmm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "project_steps")
@Getter
@Setter
public class ProjectStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    private int daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}

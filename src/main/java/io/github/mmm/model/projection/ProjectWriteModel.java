package io.github.mmm.model.projection;

import io.github.mmm.model.Project;
import io.github.mmm.model.ProjectStep;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class ProjectWriteModel {

    private int id;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public ProjectWriteModel() {
        steps.add(new ProjectStep());
    }

    public Project toProject() {
        Project project = new Project();
        project.setDescription(description);
        steps.forEach(step -> step.setProject(project));
        project.setSteps(new HashSet<>(steps));
        return project;
    }
}

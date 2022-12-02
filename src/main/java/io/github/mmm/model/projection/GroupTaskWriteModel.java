package io.github.mmm.model.projection;

import io.github.mmm.model.Task;
import io.github.mmm.model.TaskGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class GroupTaskWriteModel {

    @NotBlank(message = "Description cannot be empty")
    String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public Task toTask(TaskGroup result) {
        return new Task(description, deadline, result);
    }
}

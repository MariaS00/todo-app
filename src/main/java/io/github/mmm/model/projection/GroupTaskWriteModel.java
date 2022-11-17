package io.github.mmm.model.projection;

import io.github.mmm.model.Task;
import io.github.mmm.model.TaskGroup;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupTaskWriteModel {

    String description;
    private LocalDateTime deadline;

    public Task toTask(TaskGroup result) {
        return new Task(description, deadline, result);
    }
}

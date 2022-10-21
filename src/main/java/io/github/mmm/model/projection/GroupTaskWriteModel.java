package io.github.mmm.model.projection;

import io.github.mmm.model.Task;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupTaskWriteModel {

    String description;
    private LocalDateTime deadline;

    public Task toTask() {
        return new Task(description, deadline);
    }
}

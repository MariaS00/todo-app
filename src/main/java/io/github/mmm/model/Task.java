package io.github.mmm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task extends BaseTaskClass {

    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    public void updateFrom(final Task source) {
        super.description = source.description;
        super.done = source.done;
        deadline = source.deadline;
        group = source.group;
    }
}

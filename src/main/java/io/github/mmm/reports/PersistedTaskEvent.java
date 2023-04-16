package io.github.mmm.reports;

import io.github.mmm.model.event.TaskEvent;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor
@Entity
@Table(name = "task_events")
public class PersistedTaskEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int taskId;
    LocalDateTime occurrence;
    String name;

    PersistedTaskEvent(TaskEvent taskEvent) {
        taskId = taskEvent.getTaskId();
        name = taskEvent.getClass().getSimpleName();
        occurrence = LocalDateTime.ofInstant(taskEvent.getOccurrence(), ZoneId.systemDefault());
    }
}

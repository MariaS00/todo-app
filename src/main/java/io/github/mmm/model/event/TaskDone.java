package io.github.mmm.model.event;

import io.github.mmm.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {

    TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}

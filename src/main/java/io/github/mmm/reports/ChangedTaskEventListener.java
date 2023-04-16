package io.github.mmm.reports;

import io.github.mmm.model.event.TaskDone;
import io.github.mmm.model.event.TaskEvent;
import io.github.mmm.model.event.TaskUndone;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangedTaskEventListener {

    public static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    @Async
    @EventListener
    public void on(TaskDone event) {
        getPersistedTaskEvent(event);
    }

    @Async
    @EventListener
    public void on(TaskUndone event) {
        getPersistedTaskEvent(event);
    }

    private void getPersistedTaskEvent(TaskEvent event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }

}

package io.github.mmm.logic;

import io.github.mmm.model.Task;
import io.github.mmm.repo.TaskRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository repository;

    @Async
    public CompletableFuture<List<Task>> findAllAsync() {
        logger.info("[findAllAsync]");
        return CompletableFuture.supplyAsync(repository::findAll);
    }

    public List<Task> findTasksForToday() {
        LocalDateTime highestTodayDate = LocalDateTime.now().withHour(23).withMinute(59);
        return repository.findByDone(false).stream()
                .filter(task -> task.getDeadline() == null
                        || task.getDeadline().isBefore(highestTodayDate)
                        || task.getDeadline().toLocalDate().isEqual(highestTodayDate.toLocalDate()))
                .collect(Collectors.toList());
    }
}

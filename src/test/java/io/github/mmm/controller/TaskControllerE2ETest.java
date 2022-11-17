package io.github.mmm.controller;

import io.github.mmm.model.Task;
import io.github.mmm.repo.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("!integration")
class TaskControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TaskRepository repository;

    @Test
    void returnAllTasks() {
        int initialSize = repository.findAll().size();
        repository.save(new Task("foo", LocalDateTime.now()));
        repository.save(new Task("bar", LocalDateTime.now()));

        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        assertThat(result).hasSize(initialSize + 2);
    }

}
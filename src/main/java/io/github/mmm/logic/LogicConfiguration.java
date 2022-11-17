package io.github.mmm.logic;

import io.github.mmm.TaskConfigurationProperties;
import io.github.mmm.repo.ProjectRepository;
import io.github.mmm.repo.TaskGroupRepository;
import io.github.mmm.repo.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {

    @Bean
    ProjectService projectService(ProjectRepository repository,
                                  TaskGroupRepository groupRepository,
                                  TaskConfigurationProperties config,
                                  TaskGroupService service
    ) {
        return new ProjectService(repository, groupRepository, config, service);
    }

    @Bean
    TaskGroupService groupService(TaskGroupRepository groupRepository,
                                  TaskRepository taskRepository
    ) {
        return new TaskGroupService(groupRepository, taskRepository);
    }
}

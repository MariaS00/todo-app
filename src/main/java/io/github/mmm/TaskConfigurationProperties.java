package io.github.mmm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
@Getter
@Setter
public class TaskConfigurationProperties {

    private boolean allowMultipleTasksFromTemplate;

    private Template template;

    public boolean isAllowMultipleTasksFromTemplate() {
        return allowMultipleTasksFromTemplate;
    }

    public void setAllowMultipleTasksFromTemplate(boolean allowMultipleTasksFromTemplate) {
        this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
    }

    @Getter
    @Setter
    public static class Template {
        private boolean allowMultipleTasks;
    }
}

package io.github.mmm.model.projection;

import io.github.mmm.model.Task;
import io.github.mmm.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {

    @Test
    @DisplayName("Should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline() {
        TaskGroup source = new TaskGroup();
        source.setDescription("Note4");
        source.setTasks(Set.of(new Task("xdd", null)));

        GroupReadModel result = new GroupReadModel(source);

        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}
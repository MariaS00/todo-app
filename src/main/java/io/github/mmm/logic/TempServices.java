package io.github.mmm.logic;

import io.github.mmm.model.Task;
import io.github.mmm.repo.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempServices {

    @Autowired
    List<String> temp(TaskGroupRepository repository){
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks()
                        .stream()
                        .map(Task::getDescription))
                .collect(Collectors.toList());
    }
}

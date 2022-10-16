package io.github.mmm.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Getter
@Setter
abstract class BaseTaskClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Description cannot be empty")
    String description;
    boolean done;



}

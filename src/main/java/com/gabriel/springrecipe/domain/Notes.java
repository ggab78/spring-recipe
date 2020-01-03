package com.gabriel.springrecipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;


@Data
public class Notes {

    @Id
    private String id = UUID.randomUUID().toString();
    private String recipeNotes;

}
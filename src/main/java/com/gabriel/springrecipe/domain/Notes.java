package com.gabriel.springrecipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;


@Data
public class Notes {

    @Id
    private String id;
    private String recipeNotes;

}
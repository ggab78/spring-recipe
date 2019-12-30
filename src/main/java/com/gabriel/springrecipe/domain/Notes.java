package com.gabriel.springrecipe.domain;

import lombok.*;

import java.util.UUID;


@Data
public class Notes {

    private String id= UUID.randomUUID().toString();
    private String recipeNotes;

}
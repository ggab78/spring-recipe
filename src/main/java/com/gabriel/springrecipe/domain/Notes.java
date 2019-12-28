package com.gabriel.springrecipe.domain;

import lombok.*;

import static lombok.EqualsAndHashCode.Exclude;

@Data

public class Notes {

    private String id;

//    need to exclude recipes from hashCode because of bidirectional mapping
//    @Exclude
//    private Recipe recipe;

    private String recipeNotes;

}
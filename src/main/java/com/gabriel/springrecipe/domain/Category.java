package com.gabriel.springrecipe.domain;


import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Document
public class Category {

    private String id;
    private String description;

//    need to exclude recipes from hashCode because of bidirectional mapping
//    @Exclude
//    private Set<Recipe> recipes = new HashSet<>();

}

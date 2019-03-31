package com.gabriel.springrecipe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToMany(mappedBy = "categories")
    @Exclude // need to exclude recipes from hashCode because of bidirectional mapping
    private Set<Recipe> recipes = new HashSet<>();

}

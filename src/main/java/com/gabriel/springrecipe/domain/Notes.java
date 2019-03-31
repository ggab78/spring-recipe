package com.gabriel.springrecipe.domain;


import lombok.*;

import javax.persistence.*;

import static lombok.EqualsAndHashCode.Exclude;

@Data
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @Exclude // need to exclude recipes from hashCode because of bidirectional mapping
    private Recipe recipe;

    @Lob//permits to use large object of more than 256 characters which is standard
    private String recipeNotes;

}
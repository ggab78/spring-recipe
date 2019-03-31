package com.gabriel.springrecipe.domain;


import lombok.*;

import javax.persistence.*;

@Data
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Recipe recipe;

    @Lob//permits to use large object of more than 256 characters which is standard
    private String recipeNotes;

}
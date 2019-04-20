package com.gabriel.springrecipe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Exclude // need to exclude recipes from hashCode because of bidirectional mapping
    private Recipe recipe;

    @OneToOne (fetch = FetchType.EAGER)//EAGER is default behaviour for *ToOne mapping
    private UnitOfMeasure uom;

    private String description;
    private BigDecimal amount;

}

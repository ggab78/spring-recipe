package com.gabriel.springrecipe.domain;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
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

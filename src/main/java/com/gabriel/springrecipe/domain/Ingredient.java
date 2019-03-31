package com.gabriel.springrecipe.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recipe recipe;

    @OneToOne (fetch = FetchType.EAGER)//EAGER is default behaviour for *ToOne mapping
    private UnitOfMeasure uom;

    private String description;
    private BigDecimal amount;

}

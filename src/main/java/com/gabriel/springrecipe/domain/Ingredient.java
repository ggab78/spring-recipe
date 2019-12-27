package com.gabriel.springrecipe.domain;

import lombok.*;
import lombok.EqualsAndHashCode.Exclude;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ingredient {


    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom){
        this.uom=uom;
        this.description=description;
        this.amount=amount;
    }


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

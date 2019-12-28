package com.gabriel.springrecipe.repositories;

import com.gabriel.springrecipe.domain.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure,String> {

    Optional<UnitOfMeasure> findByDescription(String description);

}
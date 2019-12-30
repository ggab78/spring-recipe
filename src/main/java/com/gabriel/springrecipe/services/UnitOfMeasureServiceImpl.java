package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.UnitOfMeasureRepository;
import com.gabriel.springrecipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;


    @Override
    public Flux<UnitOfMeasure> findAllUom() {
      return unitOfMeasureReactiveRepository.findAll();
    }

    @Override
    public Flux<UnitOfMeasureCommand> findAllUomCommand() {
        return findAllUom().map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }
}

package com.gabriel.springrecipe.repositories.reactive;

import com.gabriel.springrecipe.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureReactiveRepositoryTest {

    public static final String SPOON = "spoon";
    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    UnitOfMeasure unitOfMeasure;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureReactiveRepository.deleteAll().block();
        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId("1");
        unitOfMeasure.setDescription(SPOON);
    }

    @Test
    public void save() throws Exception{
        Mono<UnitOfMeasure> unitOfMeasureMono = unitOfMeasureReactiveRepository.save(unitOfMeasure);
        assertEquals(unitOfMeasure.getId(), unitOfMeasureMono.block().getId());
        assertEquals((Long)1L, unitOfMeasureReactiveRepository.count().block());
    }

    @Test
    public void findByDescription() throws Exception{
        unitOfMeasureReactiveRepository.save(unitOfMeasure).block();
        Mono<UnitOfMeasure> unitOfMeasureMono = unitOfMeasureReactiveRepository.findByDescription("spoon");
        assertEquals(unitOfMeasure.getDescription(), unitOfMeasureMono.block().getDescription());
        assertEquals((Long)1L, unitOfMeasureReactiveRepository.count().block());
    }
}
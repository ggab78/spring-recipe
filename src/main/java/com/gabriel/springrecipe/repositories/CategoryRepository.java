package com.gabriel.springrecipe.repositories;

import com.gabriel.springrecipe.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}

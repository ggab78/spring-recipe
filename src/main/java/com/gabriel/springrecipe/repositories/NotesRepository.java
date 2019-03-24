package com.gabriel.springrecipe.repositories;

import com.gabriel.springrecipe.domain.Notes;
import org.springframework.data.repository.CrudRepository;

public interface NotesRepository extends CrudRepository<Notes,Long> {
}

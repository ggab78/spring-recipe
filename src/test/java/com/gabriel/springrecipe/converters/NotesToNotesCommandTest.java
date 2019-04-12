package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.NotesCommand;
import com.gabriel.springrecipe.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesToNotesCommandTest {

    NotesToNotesCommand notesToNotesCommand;

    public static final String NOTES = "notes";
    public static final Long ID = 1L;

    @Before
    public void setUp() throws Exception {
        notesToNotesCommand = new NotesToNotesCommand();
    }

    @Test
    public void testNull(){
        assertNull(notesToNotesCommand.convert(null));
    }

    @Test
    public void testEmpty(){
        assertNotNull(notesToNotesCommand.convert(new Notes()));
    }

    @Test
    public void convert() {
        //given
        Notes notes = new Notes();
        notes.setId(ID);
        notes.setRecipeNotes(NOTES);

        //when
        NotesCommand notesCommand = notesToNotesCommand.convert(notes);

        //then
        assertEquals(ID,notesCommand.getId());
        assertEquals(NOTES, notesCommand.getRecipeNotes());
    }
}
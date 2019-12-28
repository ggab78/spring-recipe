package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.NotesCommand;
import com.gabriel.springrecipe.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesCommandToNotesTest {

    NotesCommandToNotes notesCommandToNotes;

    public static final String NOTES = "notes";
    public static final String ID = "1";

    @Before
    public void setUp() throws Exception {
        notesCommandToNotes = new NotesCommandToNotes();
    }

    @Test
    public void testNull(){
        assertNull(notesCommandToNotes.convert(null));
    }

    @Test
    public void testEmpty(){
        assertNotNull(notesCommandToNotes.convert(new NotesCommand()));
    }

    @Test
    public void convert() {
        //given
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(ID);
        notesCommand.setRecipeNotes(NOTES);

        //when
        Notes notes = notesCommandToNotes.convert(notesCommand);

        //then
        assertEquals(ID,notes.getId());
        assertEquals(NOTES, notes.getRecipeNotes());
    }
}
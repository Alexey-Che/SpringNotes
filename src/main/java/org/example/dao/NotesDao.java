package org.example.dao;

import org.example.models.Note;

import java.sql.SQLException;
import java.util.List;

public interface NotesDao {

    void addNote(Note note) throws SQLException;
    List<Note> showAllNotes() throws SQLException;
    List<Note> searchBySubstring(String substring) throws SQLException;
    void deleteNote(long id) throws SQLException;
    Note showNoteById(long id) throws SQLException;
    void update(long id, Note note) throws SQLException;
}

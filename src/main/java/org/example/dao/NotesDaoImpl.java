package org.example.dao;

import org.example.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component

public class NotesDaoImpl implements NotesDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public NotesDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String SQL_GET_ALL = "SELECT * FROM spring_notes";
    private static final String SQL_SEARCH = "SELECT * FROM spring_notes WHERE LOWER(title) LIKE :titleSearch OR LOWER(text) LIKE :textSearch";
    private static final String SQL_INS_ROW = "INSERT INTO spring_notes (title, text) values (:title, :text)";
    private static final String SQL_DEL_ROW = "DELETE FROM spring_notes WHERE id = :id";
    private static final String SQL_GET_ROW = "SELECT * FROM spring_notes WHERE id = :id";
    private static final String SQL_UPD_ROW = "UPDATE spring_notes SET title= :title, text= :text WHERE id= :id";


    public Map<String, Object> createMap(Note note, Long id, String searchString) {
        String search = "%" + searchString.toLowerCase() + "%";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("titleSearch", search);
        params.put("textSearch", search);
        params.put("title", note.getTitle());
        params.put("text", note.getText());
        return params;
    }

    public Note showNoteById(long id) {
        return namedParameterJdbcTemplate.query(SQL_GET_ROW, createMap(new Note(), id, "null"), new BeanPropertyRowMapper<>(Note.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void addNote(Note note) {
        namedParameterJdbcTemplate.update(SQL_INS_ROW, createMap(note, null, "null"));
    }

    @Override
    public List<Note> showAllNotes() {
        return namedParameterJdbcTemplate.query(SQL_GET_ALL, new BeanPropertyRowMapper<>(Note.class));
    }

    @Override
    public List<Note> searchBySubstring(String substring) {
        return namedParameterJdbcTemplate.query(SQL_SEARCH, createMap(new Note(), null, substring), new BeanPropertyRowMapper<>(Note.class));
    }

    @Override
    public void deleteNote(long id) {
        namedParameterJdbcTemplate.update(SQL_DEL_ROW, createMap(new Note(), id, "null"));
    }

    @Override
    public void update(long id, Note note) {
        namedParameterJdbcTemplate.update(SQL_UPD_ROW, createMap(note, id, "null"));
    }
}

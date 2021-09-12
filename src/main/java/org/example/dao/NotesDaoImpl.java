package org.example.dao;

import org.example.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotesDaoImpl implements NotesDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_GET_ALL = "SELECT * FROM spring_notes";
    private static final String SQL_SEARCH = "SELECT * FROM spring_notes WHERE LOWER(title) LIKE '%s' OR LOWER(text) LIKE '%s'";
    private static final String SQL_INS_ROW = "INSERT INTO spring_notes (title, text) values (?, ?)";
    private static final String SQL_DEL_ROW = "DELETE FROM spring_notes WHERE id = ?";
    private static final String SQL_GET_ROW = "SELECT * FROM spring_notes WHERE id = ?";
    private static final String SQL_UPD_ROW = "UPDATE spring_notes SET title=?, text=? WHERE id=?";

    public Note showNoteById(long id) {
        return jdbcTemplate.query(SQL_GET_ROW, new Object[]{id}, new BeanPropertyRowMapper<>(Note.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void addNote(Note note) {
        jdbcTemplate.update(SQL_INS_ROW, note.getTitle(), note.getText());
    }

    @Override
    public List<Note> showAllNotes() {
        return jdbcTemplate.query(SQL_GET_ALL, new BeanPropertyRowMapper<>(Note.class));
    }

    private static String getSearchString(String searchString) {
        String search = "%" + searchString.toLowerCase() + "%";
        return String.format(SQL_SEARCH, search, search);
    }

    @Override
    public List<Note> searchBySubstring(String substring) {
        return jdbcTemplate.query(getSearchString(substring), new BeanPropertyRowMapper<>(Note.class));
    }

    @Override
    public void deleteNote(long id) {
        jdbcTemplate.update(SQL_DEL_ROW, id);
    }

    @Override
    public void update(long id, Note note) {
        jdbcTemplate.update(SQL_UPD_ROW, note.getTitle(), note.getText(), id);
    }
}

package org.example.dao;

import org.example.models.Note;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotesDaoImpl implements NotesDao {

    private Connection jdbcConnection;

    private static final String SQL_GET_ALL = "SELECT * FROM spring_notes";
    private static final String SQL_SEARCH = "SELECT * FROM spring_notes WHERE LOWER(title) LIKE '%s' OR LOWER(text) LIKE '%s'";
    private static final String SQL_INS_ROW = "INSERT INTO spring_notes (title, text) values (?, ?)";
    private static final String SQL_DEL_ROW = "DELETE FROM spring_notes WHERE id = ?";
    private static final String SQL_GET_ROW = "SELECT * FROM spring_notes WHERE id = ?";
    private static final String SQL_UPD_ROW = "UPDATE spring_notes SET title=?, text=? WHERE id=?";

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            String jdbcURL = "jdbc:postgresql://127.0.0.1:5432/spring_course?currentSchema=public";
            String jdbcUsername = "postgres";
            String jdbcPassword = "postgres";
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);

        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    public Note showNoteById(long id) throws SQLException {
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(SQL_GET_ROW);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Note note = new Note();
        note.setId(resultSet.getInt("id"));
        note.setTitle(resultSet.getString("title"));
        note.setText(resultSet.getString("text"));
        disconnect();
        return note;
    }

    @Override
    public void addNote(Note note) throws SQLException {
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(SQL_INS_ROW);
        statement.setString(1, note.getTitle());
        statement.setString(2, note.getText());
        statement.executeUpdate();
        disconnect();
    }

    @Override
    public List<Note> showAllNotes() throws SQLException {
        connect();
        List<Note> notes = new ArrayList<>();
        PreparedStatement statement = jdbcConnection.prepareStatement(SQL_GET_ALL);
        return getNotes(notes, statement);
    }

    private static String getSearchString(String searchString) {
        String search = "%" + searchString.toLowerCase() + "%";
        return String.format(SQL_SEARCH, search, search);
    }

    @Override
    public List<Note> searchBySubstring(String substring) throws SQLException {
        connect();
        List<Note> result = new ArrayList<>();
        PreparedStatement statement = jdbcConnection.prepareStatement(getSearchString(substring));
        return getNotes(result, statement);
    }

    private List<Note> getNotes(List<Note> result, PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Note note = new Note();
            note.setId(resultSet.getInt("id"));
            note.setTitle(resultSet.getString("title"));
            note.setText(resultSet.getString("text"));
            result.add(note);
        }
        disconnect();
        return result;
    }

    @Override
    public void deleteNote(long id) throws SQLException {
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(SQL_DEL_ROW);
        statement.setInt(1, Math.toIntExact(id));
        statement.executeUpdate();
        disconnect();
    }

    @Override
    public void update(long id, Note note) throws SQLException {
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(SQL_UPD_ROW);
        statement.setString(1, note.getTitle());
        statement.setString(2, note.getText());
        statement.setLong(3, id);
        statement.executeUpdate();
        disconnect();
    }
}

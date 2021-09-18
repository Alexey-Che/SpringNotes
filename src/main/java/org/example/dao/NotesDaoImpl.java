package org.example.dao;

import org.example.models.Note;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class NotesDaoImpl implements NotesDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public NotesDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void addNote(Note note) {
        Session session = sessionFactory.getCurrentSession();
        session.save(note);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> showAllNotes() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select n from Note n", Note.class).getResultList();
    }

    @Override
    @Transactional
    public List<Note> searchBySubstring(String substring) {
        Session session = sessionFactory.getCurrentSession();
        String search = "%" + substring.toLowerCase() + "%";
        return session.createQuery("select n from Note n where lower(n.title) like :string or lower(n.text) like :string", Note.class).setParameter("string", search).getResultList();
    }

    @Override
    @Transactional
    public void deleteNote(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Note.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public Note showNoteById(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Note.class, id);
    }

    @Override
    @Transactional
    public void update(long id, Note note) {
        Session session = sessionFactory.getCurrentSession();
        Note noteToBeUpdated = session.get(Note.class, id);
        noteToBeUpdated.setTitle(note.getTitle());
        noteToBeUpdated.setText(note.getText());
//        session.save(noteToBeUpdated);
    }
}

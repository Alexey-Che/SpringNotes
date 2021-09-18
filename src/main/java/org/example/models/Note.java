package org.example.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "spring_notes")
public class Note {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "title field cannot be empty")
    @Size(min = 2, max = 20, message = "title should be between 2 and 20 characters")
    @Column(name = "title")
    private String title;

    @Size(min = 1, message = "need more text")
    @Column(name = "text")
    private String text;

    public Note() {
    }

    public Note(long id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return title + "\n" + text;
    }
}

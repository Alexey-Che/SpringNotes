package org.example.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Note {
    @NotEmpty(message = "title field cannot be empty")
    @Size(min = 2, max = 20, message = "title should be between 2 and 20 characters")
    private String title;
    @Size(min = 1, message = "need more text")
    private String text;
    private long id;

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

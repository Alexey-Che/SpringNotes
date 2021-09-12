package org.example.controllers;

import org.example.dao.NotesDao;
import org.example.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private final NotesDao notesDao;

    @Autowired
    public NotesController(NotesDao notesDao) {
        this.notesDao = notesDao;
    }


    @GetMapping()
    public String listNotes(Model model) throws SQLException {
        model.addAttribute("notes", notesDao.showAllNotes());
        return "list";
    }

    @GetMapping("/{id}")
    public String showNote(@PathVariable("id") long id, Model model) throws SQLException {
        model.addAttribute("note", notesDao.showNoteById(id));
        return "show";
    }

    @GetMapping("/new")
    public String newNote(@ModelAttribute("note") Note note) {
        return "new";
    }

    @PostMapping
    public String createNote(@ModelAttribute("note") @Valid Note note,
                             BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) return "new";
        notesDao.addNote(note);
        return "redirect:/notes";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") long id) throws SQLException {
        model.addAttribute("note", notesDao.showNoteById(id));
        return "edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("note") @Valid Note note,
                         BindingResult bindingResult,
                         @PathVariable("id") long id) throws SQLException {
        if (bindingResult.hasErrors()) return "edit";
        notesDao.update(id, note);
        return "redirect:/notes";
    }

    @PostMapping("/remove/{id}")
    public String delete(@PathVariable("id") long id) throws SQLException {
        notesDao.deleteNote(id);
        return "redirect:/notes";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") long id) throws SQLException {
        notesDao.deleteNote(id);
        return "redirect:/notes";
    }

    @GetMapping("/search")
    public String searchBySubstring(@RequestParam(value = "substring", required = false) String substring,
                                    Model model) throws SQLException {
        if (substring == null || substring.trim().isEmpty()) {
            return "redirect:/notes";
        } else {
            model.addAttribute("notes", notesDao.searchBySubstring(substring));
        }
        return "list";
    }

    @GetMapping("/notes/search")
    public String searchAfterSearch(@RequestParam(value = "substring", required = false) String substring) {
        return "redirect:/notes/search?substring="+ substring;
    }
}

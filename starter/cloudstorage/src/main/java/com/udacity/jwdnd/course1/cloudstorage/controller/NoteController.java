package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteService noteService;

    @PostMapping("/insert-or-update")
    public String insertOrUpdateNote(@RequestParam("noteId") Integer noteId,
                                     @RequestParam("noteTitle") String noteTitle,
                                     @RequestParam("noteDescription") String noteDescription,
                                     RedirectAttributes redirectAttributes) {
        String errorMsg = null;

        try {
            if (noteId != null) {
                int rowsUpdated = noteService.updateNote(noteId, noteTitle, noteDescription);
                if (rowsUpdated < 0) {
                    errorMsg = "Có lỗi trong khi update note !";
                }
            } else {
                int rowsAdded = noteService.insertNote(noteTitle, noteDescription);
                if (rowsAdded < 0) {
                    errorMsg = "Có lỗi trong khi insert note !";
                }
            }

            if (errorMsg != null) {
                redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
            } else {
                redirectAttributes.addFlashAttribute("isSuccess", true);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi không xác định khi insert/update note!");
        }
        return "redirect:/home?activeTab=notes";
    }

    @PostMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes){
        try{
            noteService.deleteNoteById(noteId);
            redirectAttributes.addFlashAttribute("isSuccess", true);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi không xác định khi xóa note!");
        }
        return "redirect:/home?activeTab=notes";
    }
}

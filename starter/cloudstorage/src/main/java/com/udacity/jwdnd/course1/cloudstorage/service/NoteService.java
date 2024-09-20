package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    NoteMapper noteMapper;

    public Note[] getNotesForCurrentUser() {
        int currentUserId = getCurrentUserId();
        return noteMapper.getNotesByUserId(currentUserId);
    }

    public int updateNote(Integer noteId, String noteTitle, String noteDescription) throws Exception {
        int currentUserId = getCurrentUserId();
        Note noteFinded = getNoteById(noteId);
        if (noteFinded == null) {
            throw new Exception("Note not exists !");
        }
        noteFinded.setNoteTitle(noteTitle);
        noteFinded.setNoteDescription(noteDescription);
        return noteMapper.updateNote(noteFinded, currentUserId);
    }

    public int insertNote(String noteTitle, String noteDescription) throws Exception {
        int currentUserId = getCurrentUserId();
        Note note = new Note(null, noteTitle, noteDescription, currentUserId);
        return noteMapper.insertNote(note);
    }

    public void deleteNoteById(Integer noteId){
        int currentUserId = getCurrentUserId();
        noteMapper.deleteNoteById(noteId, currentUserId);
    }


    private int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();

        return userMapper.getUser(username).getUserId();
    }

    private Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }
}

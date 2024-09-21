package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    FileService fileService;

    @Autowired
    NoteService noteService;

    @Autowired
    CredentialService credentialService;

    @GetMapping
    public String getHomePage() {
        return "home";
    }


    @ModelAttribute("allFiles")
    public File[] allFiles() {
        return fileService.getFilesForCurrentUser();
    }

    @ModelAttribute("allNotes")
    public Note[] allNotes() {
        return noteService.getNotesForCurrentUser();
    }

    @ModelAttribute("allCredentials")
    public CredentialDTO[] allCredentials() {
        return credentialService.getCredentialsForCurrentUser();
    }

}

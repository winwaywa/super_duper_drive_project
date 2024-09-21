package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    @Autowired
    CredentialService credentialService;

    @PostMapping("/insert-or-update")
    public String insertOrUpdateCredential(@RequestParam("credentialId") Integer credentialId,
                                         @RequestParam("url") String url,
                                         @RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         RedirectAttributes redirectAttributes) {
        String errorMsg = null;

        try {
            if (credentialId != null) {
                int rowsUpdated = credentialService.updateCredential(credentialId, url, username, password);
                if (rowsUpdated < 0) {
                    errorMsg = "Có lỗi trong khi update credential !";
                }
            } else {
                int rowsAdded = credentialService.insertCredential( url, username, password);
                if (rowsAdded < 0) {
                    errorMsg = "Có lỗi trong khi insert credential !";
                }
            }

            if (errorMsg != null) {
                redirectAttributes.addFlashAttribute("errorMessage", errorMsg);
            } else {
                redirectAttributes.addFlashAttribute("isSuccess", true);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi không xác định khi insert/update credential!");
        }
        return "redirect:/home?activeTab=credentials";
    }

    @PostMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes){
        try{
            credentialService.deleteCredentialById(credentialId);
            redirectAttributes.addFlashAttribute("isSuccess", true);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi không xác định khi xóa credential!");
        }
        return "redirect:/home?activeTab=credentials";
    }
}

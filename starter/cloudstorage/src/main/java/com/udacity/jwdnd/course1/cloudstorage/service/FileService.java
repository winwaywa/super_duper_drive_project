package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    @Autowired
    FileMapper fileMapper;

    @Autowired
    UserMapper userMapper;

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFileById(Integer fileId) {
        int currentUserId = getCurrentUserId();
        fileMapper.deleteFileById(fileId, currentUserId);
    }

    public void saveFile(@NotNull MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("File không hợp lệ hoặc chưa được chọn.");
        }
        int currentUserId = getCurrentUserId();
        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setContentType(file.getContentType());
        newFile.setFileSize(String.valueOf(file.getSize()));
        newFile.setUserid(currentUserId);
        newFile.setFileData(file.getBytes());
        fileMapper.insertFile(newFile);
    }

    public File[] getFilesForCurrentUser() {
        int currentUserId = getCurrentUserId();
        return fileMapper.getFilesByUserId(currentUserId);
    }

    private int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();

        return userMapper.getUser(username).getUserId();
    }
}

package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDTO;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CredentialService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CredentialMapper credentialMapper;

    @Autowired
    EncryptionService encryptionService;

    public CredentialDTO[] getCredentialsForCurrentUser() {
        int currentUserId = getCurrentUserId();
        Credential[] credentialList = credentialMapper.getCredentialsByUserId(currentUserId);
        return Arrays.stream(credentialList)
                .map(credential -> new CredentialDTO(credential, encryptionService.decryptValue(credential.getPassword(), credential.getKey())))
                .toArray(CredentialDTO[]::new);
    }

    public int updateCredential(Integer credentialId, String url, String username, String password) throws Exception {
        int currentUserId = getCurrentUserId();
        Credential credentialFinded = getCredentialById(credentialId);
        if (credentialFinded == null) {
            throw new Exception("Credential not exists !");
        }

        String encodedKey = credentialFinded.getKey();
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        credentialFinded.setUrl(url);
        credentialFinded.setUsername(username);
        credentialFinded.setPassword(encryptedPassword);
        return credentialMapper.updateCredential(credentialFinded, currentUserId);
    }

    public int insertCredential(String url, String username, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        int currentUserId = getCurrentUserId();

        Credential credential = new Credential(null, url, username, encodedKey, encryptedPassword, currentUserId);
        return credentialMapper.insertCredential(credential);
    }

    public void deleteCredentialById(Integer credentialId) {
        int currentUserId = getCurrentUserId();
        credentialMapper.deleteCredentialById(credentialId, currentUserId);
    }


    private int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();

        return userMapper.getUser(username).getUserId();
    }

    private Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }
}

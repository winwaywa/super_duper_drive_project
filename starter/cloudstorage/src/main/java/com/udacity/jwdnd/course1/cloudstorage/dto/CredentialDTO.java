package com.udacity.jwdnd.course1.cloudstorage.dto;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

public class CredentialDTO extends Credential {

    private String decryptedPassword;

    public CredentialDTO(Credential credential, String decryptedPassword) {
        super(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), credential.getKey(), credential.getPassword(), credential.getUserid());
        this.decryptedPassword = decryptedPassword;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }
}

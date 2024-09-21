package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    Credential[] getCredentialsByUserId(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credential getCredentialById(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url = #{credential.url}, username=#{credential.username}, key=#{credential.key}, password=#{credential.password}  " +
            "WHERE credentialid = #{credential.credentialId} AND userid = #{currentUserId}")
    int updateCredential(Credential credential, int currentUserId);

    @Insert("INSERT INTO CREDENTIALS(url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
    void deleteCredentialById(Integer credentialId, int userId);
}

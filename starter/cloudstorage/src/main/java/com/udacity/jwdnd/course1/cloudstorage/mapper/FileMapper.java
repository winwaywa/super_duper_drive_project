package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    File[] getFilesByUserId(int userid);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(int fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} AND userid = #{userid}")
    void deleteFileById(int fileId, int userid);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userid}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void insertFile(File file);
}

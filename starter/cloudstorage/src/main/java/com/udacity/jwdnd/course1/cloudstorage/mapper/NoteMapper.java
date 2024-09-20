package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotesByUserId(int userId);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNoteById(Integer noteId);

    @Update("UPDATE NOTES SET notetitle = #{note.noteTitle}, notedescription=#{note.noteDescription}  WHERE noteid = #{note.noteId} AND userid = #{currentUserId}")
    int updateNote(Note note, int currentUserId);

    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) VALUES(${noteTitle}, #{noteDescription}, #{userId}) ")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userid = #{userid}")
    void deleteNoteById(Integer noteId, int userid);
}

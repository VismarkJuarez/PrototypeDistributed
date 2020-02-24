package com.example.myapplication.DAOs

import androidx.room.*
import com.example.myapplication.*

// Thanks to Gunnar Bernstein https://stackoverflow.com/questions/49322313/android-room-generic-dao
// Thanks to Dnail Alexiev https://stackoverflow.com/questions/51972843/polymorphic-entities-in-room

@Dao
interface BaseDao<T>{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>?): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T?): Long

    @Delete
    suspend fun delete(obj: T?)

    @Delete
    suspend fun delete(obj: List<T>?)

    @Update
    suspend fun update(obj: List<T>?): Int

    @Update
    suspend fun update(obj: T?): Int
}
@Dao
interface QuestionDao : BaseDao<Question>  {

    @Query("SELECT * from Question")
    fun getAllQuestions(): List<Question>

    @Query("DELETE FROM Question")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM Question WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): List<Question>

    @Query("SELECT * FROM Question WHERE obj_id = :id")
    suspend fun getQuestion(id: Long): Question
}

@Dao
interface ResponseDao : BaseDao<Response>  {

    @Query("SELECT * from Response")
    fun getAllResponses(): List<Response>

    @Query("DELETE FROM Response")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM Response WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<Response>

    @Query("SELECT * FROM Response WHERE obj_id = :id")
    suspend fun getResponse(id: Long): Response

    @Query("SELECT * FROM Response WHERE parent_question_id = :question_id")
    suspend fun getResponsesByQuestionID(question_id: Long): List<Response>
}
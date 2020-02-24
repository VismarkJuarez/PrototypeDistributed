package com.example.myapplication.DAOs

import androidx.room.*
import com.example.myapplication.*

// TODO redo using a single template class.

@Dao
// Thanks to Gunnar Bernstein https://stackoverflow.com/questions/49322313/android-room-generic-dao
// Thanks to Dnail Alexiev https://stackoverflow.com/questions/51972843/polymorphic-entities-in-room

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
interface MultipleChoiceQuestionDao : BaseDao<MultipleChoiceQuestion>  {

    @Query("SELECT * from MultipleChoiceQuestion")
    fun getAllQuestions(): List<MultipleChoiceQuestion>

    @Query("DELETE FROM MultipleChoiceQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion>
}

@Dao
interface MatchingQuestionDao : BaseDao<MatchingQuestion> {

    @Query("SELECT * from MatchingQuestion")
    fun getAllQuestions(): List<MatchingQuestion>

    @Query("DELETE FROM MatchingQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM MatchingQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): List<MatchingQuestion>
}

@Dao
interface ShortAnswerDao : BaseDao<ShortAnswerQuestion> {

    @Query("SELECT * from ShortAnswerQuestion")
    fun getAllQuestions(): List<ShortAnswerQuestion>

    @Query("DELETE FROM ShortAnswerQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM ShortAnswerQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>) : List<ShortAnswerQuestion>
}

@Dao
interface FillInTheBlankDao :BaseDao<FillInTheBlankQuestion>{

    @Query("SELECT * from FillInTheBlankQuestion")
    fun getAllQuestions(): List<FillInTheBlankQuestion>

    @Query("DELETE FROM FillInTheBlankQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM FillInTheBlankQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): List<FillInTheBlankQuestion>
}

@Dao
interface ShortAnswerResponseDao : BaseDao<ShortAnswerResponse>{

    @Query("SELECT * from ShortAnswerResponse")
    fun getAllResponses(): List<ShortAnswerResponse>

    @Query("DELETE FROM ShortAnswerResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM ShortAnswerResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<ShortAnswerResponse>
}

@Dao
interface MultipleChoiceResponseDao : BaseDao<MultipleChoiceResponse> {

    @Query("SELECT * from MultipleChoiceResponse")
    fun getAllResponses(): List<MultipleChoiceResponse>

    @Query("DELETE FROM MultipleChoiceResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM MultipleChoiceResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse>
}

@Dao
interface FillInTheBlankResponseDao : BaseDao<FillInTheBlankResponse> {

    @Query("SELECT * from FillInTheBlankResponse")
    fun getAllResponses(): List<FillInTheBlankResponse>

    @Query("DELETE FROM FillInTheBlankResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM FillInTheBlankResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<FillInTheBlankResponse>
}

@Dao
interface MatchingResponseDao: BaseDao<MatchingResponse> {

    @Query("SELECT * from MatchingResponse")
    fun getAllResponses(): List<MatchingResponse>

    @Query("DELETE FROM MatchingResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM MatchingResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<MatchingResponse>
}
package com.example.myapplication.DAOs

import androidx.room.*
import com.example.myapplication.*

// TODO redo using a single template class.

@Dao
interface MultipleChoiceQuestionDao  {

    @Query("SELECT * from MultipleChoiceQuestion")
    fun getAllQuestions(): List<MultipleChoiceQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: MultipleChoiceQuestion)

    @Query("DELETE FROM MultipleChoiceQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion>

    @Insert
    suspend fun insertAllQuestions(vararg questions: MultipleChoiceQuestion)

    @Delete
    suspend fun deleteQuestion(question: MultipleChoiceQuestion)
}

@Dao
interface MatchingQuestionDao {

    @Query("SELECT * from MatchingQuestion")
    fun getAllQuestions(): List<MatchingQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: MatchingQuestion)

    @Query("DELETE FROM MatchingQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM MatchingQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>) : List<MatchingQuestion>

    @Insert
    suspend fun insertAllQuestions(vararg questions: MatchingQuestion)

    @Delete
    suspend fun deleteQuestion(question: MatchingQuestion)
}

@Dao
interface ShortAnswerDao {

    @Query("SELECT * from ShortAnswerQuestion")
    fun getAllQuestions(): List<ShortAnswerQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(question: ShortAnswerQuestion)

    @Query("DELETE FROM ShortAnswerQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM ShortAnswerQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>) : List<ShortAnswerQuestion>

    @Insert
    suspend fun insertAllQuestions(vararg questions: ShortAnswerQuestion)

    @Delete
    suspend fun deleteQuestion(question: ShortAnswerQuestion)
}

@Dao
interface FillInTheBlankDao {

    @Query("SELECT * from FillInTheBlankQuestion")
    fun getAllQuestions(): List<FillInTheBlankQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: FillInTheBlankQuestion)

    @Query("DELETE FROM FillInTheBlankQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM FillInTheBlankQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): List<FillInTheBlankQuestion>

    @Insert
    suspend fun insertAllQuestions(vararg questions: FillInTheBlankQuestion)

    @Delete
    suspend fun deleteQuestion(question: FillInTheBlankQuestion)
}

@Dao
interface ShortAnswerResponseDao {

    @Query("SELECT * from ShortAnswerResponse")
    fun getAllResponses(): List<ShortAnswerResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: ShortAnswerResponse)

    @Query("DELETE FROM ShortAnswerResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM ShortAnswerResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<ShortAnswerResponse>

    @Insert
    suspend fun insertAllResponses(vararg responses: ShortAnswerResponse)

    @Delete
    suspend fun deleteResponses(response: ShortAnswerResponse)
}

@Dao
interface MultipleChoiceResponseDao {

    @Query("SELECT * from MultipleChoiceResponse")
    fun getAllResponses(): List<MultipleChoiceResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: MultipleChoiceResponse)

    @Query("DELETE FROM MultipleChoiceResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM MultipleChoiceResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse>

    @Insert
    suspend fun insertAllResponses(vararg responses: MultipleChoiceResponse)

    @Delete
    suspend fun deleteResponse(response: MultipleChoiceResponse)
}

@Dao
interface FillInTheBlankResponseDao {

    @Query("SELECT * from FillInTheBlankResponse")
    fun getAllResponses(): List<FillInTheBlankResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: FillInTheBlankResponse)

    @Query("DELETE FROM FillInTheBlankResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM FillInTheBlankResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<FillInTheBlankResponse>

    @Insert
    suspend fun insertAllResponses(vararg responses: FillInTheBlankResponse)

    @Delete
    suspend fun deleteResponse(response: FillInTheBlankResponse)
}

@Dao
interface MatchingResponseDao {

    @Query("SELECT * from MatchingResponse")
    fun getAllResponses(): List<MatchingResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: MatchingResponse)

    @Query("DELETE FROM MatchingResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM MatchingResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<MatchingResponse>

    @Insert
    suspend fun insertAllResponses(vararg responses: MatchingResponse)

    @Delete
    suspend fun deleteResponse(response: MatchingResponse)
}
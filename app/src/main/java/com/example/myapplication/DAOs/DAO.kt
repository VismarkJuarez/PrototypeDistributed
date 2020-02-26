package com.example.myapplication.DAOs

import androidx.lifecycle.MutableLiveData
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
    suspend fun update(obj: List<T>?): List<Int>

    @Update
    suspend fun update(obj: T?): Int
}
@Dao
interface QuestionDao : BaseDao<MultipleChoiceQuestion>  {

    @Query("SELECT * from MultipleChoiceQuestion")
    fun getAllQuestions(): MutableLiveData<List<MultipleChoiceQuestion>>

    @Query("DELETE FROM MultipleChoiceQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE obj_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): MutableLiveData<List<MultipleChoiceQuestion>>

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE obj_id = :id")
    suspend fun getQuestion(id: Long): MutableLiveData<MultipleChoiceQuestion>
}

@Dao
interface ResponseDao : BaseDao<MultipleChoiceResponse>  {

    @Query("SELECT * from MultipleChoiceResponse")
    fun getAllResponses(): MutableLiveData<List<MultipleChoiceResponse>>

    @Query("DELETE FROM MultipleChoiceResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM MultipleChoiceResponse WHERE obj_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): MutableLiveData<List<MultipleChoiceResponse>>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE obj_id = :id")
    suspend fun getResponse(id: Long): MutableLiveData<MultipleChoiceResponse>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id")
    suspend fun getResponsesByQuestionID(question_id: Long): MutableLiveData<List<MultipleChoiceResponse>>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id AND quiz_id = :quiz_id")
    suspend fun getResponsesByQuestionIDAndQuizID(question_id: Long, quiz_id: Long) : MutableLiveData<List<MultipleChoiceResponse>>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id AND quiz_id = :quiz_id AND user_id = :user_id")
    suspend fun getResponsesByQuestionIDAndQuizIDAndUserID(question_id: Long, quiz_id: Long, user_id: Long): MutableLiveData<List<MultipleChoiceResponse>>
}

@Dao
interface QuizWithQuestionsDao: BaseDao<QuizWithQuestions> {
    @Transaction
    @Query("SELECT * FROM Quiz WHERE quiz_id =:quiz_id")
    suspend fun getQuizWithQuestions(quiz_id: Long): MutableLiveData<List<QuizWithQuestions>>
}

@Dao
interface UserDao: BaseDao<User> {
    @Query("SELECT * FROM User where user_id =:user_id")
    suspend fun getUser(user_id: Long): MutableLiveData<User>
}
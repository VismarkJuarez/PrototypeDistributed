package com.example.myapplication.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.*

// Thanks to Gunnar Bernstein https://stackoverflow.com/questions/49322313/android-room-generic-dao
// Thanks to Dnail Alexiev https://stackoverflow.com/questions/51972843/polymorphic-entities-in-room

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>?): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T?): Long

    @Delete
    suspend fun delete(obj: T?)

    @Delete
    suspend fun delete(obj: List<T>?)

    @Update
    suspend fun update(obj: T?): Int
}
@Dao
interface QuestionDao : BaseDao<MultipleChoiceQuestion>  {

    @Query("SELECT * from MultipleChoiceQuestion")
    fun getAllQuestions(): List<MultipleChoiceQuestion>

    @Query("DELETE FROM MultipleChoiceQuestion")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE question_id in (:id_list)")
    suspend fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion>

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE question_id = :id")
    suspend fun getQuestion(id: Long): MultipleChoiceQuestion
}

@Dao
interface ResponseDao : BaseDao<MultipleChoiceResponse>  {

    @Query("SELECT * from MultipleChoiceResponse")
    fun getAllResponses(): List<MultipleChoiceResponse>

    @Query("DELETE FROM MultipleChoiceResponse")
    suspend fun deleteAllResponses()

    @Query("SELECT * FROM MultipleChoiceResponse WHERE response_id in (:id_list)")
    suspend fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE response_id = :id")
    suspend fun getResponse(id: Long): MultipleChoiceResponse

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id")
    suspend fun getResponsesByQuestionID(question_id: Long): List<MultipleChoiceResponse>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id AND quiz_id = :quiz_id")
    suspend fun getResponsesByQuestionIDAndQuizID(question_id: Long, quiz_id: Long) : List<MultipleChoiceResponse>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id AND quiz_id = :quiz_id AND user_id = :user_id")
    suspend fun getResponsesByQuestionIDAndQuizIDAndUserID(question_id: Long, quiz_id: Long, user_id: Long): List<MultipleChoiceResponse>
}

// Thanks a ton to https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room
// This really helped deal with some design issues we'd face and simplified things
@Dao
abstract class QuizDao: BaseDao<Quiz> {

    @Insert
    abstract suspend fun insertQuestions(questions: List<MultipleChoiceQuestion>)

    @Query("SELECT * FROM QUIZ WHERE quiz_id = quiz_id")
    abstract suspend fun getQuiz(quiz_id: Long): Quiz

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE quiz_id = quiz_id")
    abstract suspend fun getQuestionList(quiz_id: Long): List<MultipleChoiceQuestion>

    @Transaction
    suspend fun getQuizWithQuestions(quiz_id: Long): Quiz{
        val quiz = getQuiz(quiz_id)
        quiz.questions = getQuestionList(quiz_id)
        return quiz
    }

    @Transaction
    suspend fun insertQuizWithQuestions(quiz: Quiz, questions: List<MultipleChoiceQuestion>){
        insert(quiz)
        insertQuestions(questions)
    }

}

@Dao
interface UserDao: BaseDao<User> {
    @Query("SELECT * FROM User where user_id =:user_id")
    suspend fun getUser(user_id: Long): User
}
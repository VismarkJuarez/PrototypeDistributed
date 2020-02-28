package com.example.myapplication.DAOs

import androidx.room.*
import com.example.myapplication.Models.MultipleChoiceQuestion
import com.example.myapplication.Models.MultipleChoiceResponse
import com.example.myapplication.Models.Quiz
import com.example.myapplication.Models.User

// Thanks to Gunnar Bernstein https://stackoverflow.com/questions/49322313/android-room-generic-dao
// Thanks to Dnail Alexiev https://stackoverflow.com/questions/51972843/polymorphic-entities-in-room

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(obj: List<T>?): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(obj: T?): Long

    @Delete
     fun delete(obj: T?)

    @Delete
     fun delete(obj: List<T>?)

    @Update
     fun update(obj: T?): Int
}
@Dao
interface QuestionDao : BaseDao<MultipleChoiceQuestion>  {

    @Query("SELECT * from MultipleChoiceQuestion")
    fun getAllQuestions(): List<MultipleChoiceQuestion>

    @Query("DELETE FROM MultipleChoiceQuestion")
     fun deleteAllQuestions()

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE question_id in (:id_list)")
     fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion>

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE question_id = :id")
     fun getQuestion(id: Long): MultipleChoiceQuestion
}

@Dao
interface ResponseDao : BaseDao<MultipleChoiceResponse>  {

    @Query("SELECT * from MultipleChoiceResponse")
    fun getAllResponses(): List<MultipleChoiceResponse>

    @Query("DELETE FROM MultipleChoiceResponse")
     fun deleteAllResponses()

    @Query("SELECT * FROM MultipleChoiceResponse WHERE response_id in (:id_list)")
     fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE response_id = :id")
    fun getResponse(id: Long): MultipleChoiceResponse

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id")
     fun getResponsesByQuestionID(question_id: Long): List<MultipleChoiceResponse>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id AND quiz_id = :quiz_id")
     fun getResponsesByQuestionIDAndQuizID(question_id: Long, quiz_id: Long) : List<MultipleChoiceResponse>

    @Query("SELECT * FROM MultipleChoiceResponse WHERE parent_question_id = :question_id AND quiz_id = :quiz_id AND user_id = :user_id")
     fun getResponsesByQuestionIDAndQuizIDAndUserID(question_id: Long, quiz_id: Long, user_id: Long): List<MultipleChoiceResponse>
}

// Thanks a ton to https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room
// This really helped deal with some design issues we'd face and simplified things
@Dao
abstract class QuizDao: BaseDao<Quiz> {

    @Insert
    abstract  fun insertQuestions(questions: List<MultipleChoiceQuestion>)

    @Query("SELECT * FROM QUIZ WHERE quiz_id = :quiz_id")
    abstract  fun getQuiz(quiz_id: Long): Quiz

    @Query("SELECT * FROM MultipleChoiceQuestion WHERE quiz_id = :quiz_id")
    abstract  fun getQuestionList(quiz_id: Long): List<MultipleChoiceQuestion>

     fun getQuizWithQuestions(quiz_id: Long): Quiz {
        val quiz = getQuiz(quiz_id)
        quiz.questions = getQuestionList(quiz_id)
        return quiz
    }

     fun insertQuizWithQuestions(quiz: Quiz, questions: List<MultipleChoiceQuestion>){
        insert(quiz)
        insertQuestions(questions)
    }

}

@Dao
interface UserDao: BaseDao<User> {
    @Query("SELECT * FROM User where user_id =:user_id")
     fun getUser(user_id: Long): User
}
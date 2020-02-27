package com.example.myapplication.DAOs

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.*

interface Repository{
    suspend fun getQuestion(id: Long): MultipleChoiceQuestion
    suspend fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion>
    suspend fun insertAllQuestions(vararg multipleChoiceQuestions: MultipleChoiceQuestion)
    suspend fun insertQuestion(multipleChoiceQuestion: MultipleChoiceQuestion)
    suspend fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse>
    suspend fun getResponse(id: Long): MultipleChoiceResponse
    suspend fun getResponsesByQuestionId(question_id: Long): List<MultipleChoiceResponse>
    suspend fun insertResponse(multipleChoiceResponse: MultipleChoiceResponse)
    suspend fun insertResponses(vararg respons: MultipleChoiceResponse)
    suspend fun getResponsesByQuizIdAndQuestionID(quiz_id: Long, question_id: Long): List<MultipleChoiceResponse>
    suspend fun getResponsesByQuizIdQuestionIDAndUserId(quiz_id: Long, question_id: Long, user_id: Long): List<MultipleChoiceResponse>
    suspend fun getUser(user_id: Long): User
    suspend fun getQuiz(quiz_id: Long): Quiz
    suspend fun insertQuiz(quiz: Quiz)
}

class RepositoryImpl (
    private val question_dao: QuestionDao,
    private val response_dao: ResponseDao,
    private val user_dao: UserDao,
    private val quiz_dao: QuizDao

): Repository{

    override suspend fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion>{
        return question_dao.getQuestions(id_list)
    }

    override suspend fun insertAllQuestions(vararg multipleChoiceQuestions: MultipleChoiceQuestion) {
        val listOfQuestions = multipleChoiceQuestions.toList()
        question_dao.insert(listOfQuestions)
        }

    override suspend fun insertQuestion(multipleChoiceQuestion: MultipleChoiceQuestion) {
        question_dao.insert(multipleChoiceQuestion)
    }

    override suspend fun getQuestion(id: Long): MultipleChoiceQuestion {
        return question_dao.getQuestion(id)
    }

    override suspend fun getResponse(id: Long): MultipleChoiceResponse {
        return response_dao.getResponse(id)
    }

    override suspend fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse> {
        return response_dao.getResponses(id_list)
    }

    override suspend fun getResponsesByQuestionId(question_id: Long): List<MultipleChoiceResponse> {
        return response_dao.getResponsesByQuestionID(question_id)
    }

    override suspend fun insertResponse(multipleChoiceResponse: MultipleChoiceResponse) {
        response_dao.insert(multipleChoiceResponse)
    }

    override suspend fun insertResponses(vararg respons: MultipleChoiceResponse) {
        response_dao.insert(respons.toList())
    }

    override suspend fun getResponsesByQuizIdAndQuestionID(
        quiz_id: Long,
        question_id: Long
    ): List<MultipleChoiceResponse> {
        return response_dao.getResponsesByQuestionIDAndQuizID(question_id, quiz_id)
    }

    override suspend fun getResponsesByQuizIdQuestionIDAndUserId(
        quiz_id: Long,
        question_id: Long,
        user_id: Long
    ): List<MultipleChoiceResponse> {
        return response_dao.getResponsesByQuestionIDAndQuizIDAndUserID(question_id, quiz_id, user_id)
    }

    override suspend fun getUser(user_id: Long): User {
        return user_dao.getUser(user_id)
    }

    override suspend fun getQuiz(quiz_id: Long): Quiz {
        return quiz_dao.getQuizWithQuestions(quiz_id)
    }

    override suspend fun insertQuiz(quiz: Quiz){
        return quiz_dao.insertQuizWithQuestions(quiz, quiz.questions)
    }
}
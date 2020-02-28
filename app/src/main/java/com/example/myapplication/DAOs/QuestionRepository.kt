package com.example.myapplication.DAOs

import com.example.myapplication.Models.MultipleChoiceQuestion
import com.example.myapplication.Models.MultipleChoiceResponse
import com.example.myapplication.Models.Quiz
import com.example.myapplication.Models.User

interface Repository{
     fun getQuestion(id: Long): MultipleChoiceQuestion?
     fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion?>
     fun insertAllQuestions(vararg multipleChoiceQuestions: MultipleChoiceQuestion)
     fun insertQuestion(multipleChoiceQuestion: MultipleChoiceQuestion)
     fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse?>
    fun getResponse(id: Long): MultipleChoiceResponse?
     fun getResponsesByQuestionId(question_id: Long): List<MultipleChoiceResponse?>
     fun insertResponse(multipleChoiceResponse: MultipleChoiceResponse)
     fun insertResponses(vararg respons: MultipleChoiceResponse)
     fun getResponsesByQuizIdAndQuestionID(quiz_id: Long, question_id: Long): List<MultipleChoiceResponse?>
     fun getResponsesByQuizIdQuestionIDAndUserId(quiz_id: Long, question_id: Long, user_id: Long): List<MultipleChoiceResponse?>
     fun getUser(user_id: Long): User?
     fun getQuiz(quiz_id: Long): Quiz?
     fun insertQuiz(quiz: Quiz)
}

class RepositoryImpl (
    private val question_dao: QuestionDao,
    private val response_dao: ResponseDao,
    private val user_dao: UserDao,
    private val quiz_dao: QuizDao

): Repository{

    override  fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion?>{
        return question_dao.getQuestions(id_list)
    }

    override  fun insertAllQuestions(vararg multipleChoiceQuestions: MultipleChoiceQuestion) {
        val listOfQuestions = multipleChoiceQuestions.toList()
        question_dao.insert(listOfQuestions)
        }

    override  fun insertQuestion(multipleChoiceQuestion: MultipleChoiceQuestion) {
        question_dao.insert(multipleChoiceQuestion)
    }

    override  fun getQuestion(id: Long): MultipleChoiceQuestion? {
        return question_dao.getQuestion(id)
    }

    override fun getResponse(id: Long): MultipleChoiceResponse? {
        return response_dao.getResponse(id)
    }

    override  fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse?> {
        return response_dao.getResponses(id_list)
    }

    override  fun getResponsesByQuestionId(question_id: Long): List<MultipleChoiceResponse?> {
        return response_dao.getResponsesByQuestionID(question_id)
    }

    override  fun insertResponse(multipleChoiceResponse: MultipleChoiceResponse) {
        response_dao.insert(multipleChoiceResponse)
    }

    override  fun insertResponses(vararg respons: MultipleChoiceResponse) {
        response_dao.insert(respons.toList())
    }

    override  fun getResponsesByQuizIdAndQuestionID(
        quiz_id: Long,
        question_id: Long
    ): List<MultipleChoiceResponse?> {
        return response_dao.getResponsesByQuestionIDAndQuizID(question_id, quiz_id)
    }

    override  fun getResponsesByQuizIdQuestionIDAndUserId(
        quiz_id: Long,
        question_id: Long,
        user_id: Long
    ): List<MultipleChoiceResponse?> {
        return response_dao.getResponsesByQuestionIDAndQuizIDAndUserID(question_id, quiz_id, user_id)
    }

    override  fun getUser(user_id: Long): User? {
        return user_dao.getUser(user_id)
    }

    override  fun getQuiz(quiz_id: Long): Quiz? {
        return quiz_dao.getQuizWithQuestions(quiz_id)
    }

    override  fun insertQuiz(quiz: Quiz){
        return quiz_dao.insertQuizWithQuestions(quiz, quiz.questions)
    }
}

class Cache: Repository{

    val questions = hashMapOf<Long, MultipleChoiceQuestion>()
    val response_list = hashMapOf<Long, MultipleChoiceResponse>()

    // User Submitted Questions
    val submitted_questions = hashMapOf<String, MultipleChoiceQuestion>()
    val users_list = hashMapOf<Long, User>()

    override  fun getQuestion(id: Long): MultipleChoiceQuestion? {
        return questions[id]
    }

    override  fun getQuestions(id_list: List<Long>): List<MultipleChoiceQuestion?> {
        return questions.values.toList()
    }

    override  fun getQuiz(quiz_id: Long): Quiz? {
        return Quiz(quiz_id = quiz_id, quiz_name = "Unnamed", questions = questions.values.toList())
    }

    override fun getResponse(id: Long): MultipleChoiceResponse? {
        return response_list.get(12)
    }

    override  fun getResponses(id_list: List<Long>): List<MultipleChoiceResponse?> {
        return response_list.values.toList()
    }

    override  fun getResponsesByQuestionId(question_id: Long): List<MultipleChoiceResponse?> {
        return response_list.values.toList().filter { it.parent_question_id == question_id }
    }

    override  fun getResponsesByQuizIdAndQuestionID(
        quiz_id: Long,
        question_id: Long
    ): List<MultipleChoiceResponse?> {
        return response_list.values.toList().filter{ (it.parent_question_id == question_id) and (it.quiz_id == quiz_id)}
    }

    override  fun getResponsesByQuizIdQuestionIDAndUserId(
        quiz_id: Long,
        question_id: Long,
        user_id: Long
    ): List<MultipleChoiceResponse?> {
        return response_list.values.toList().filter{ (it.parent_question_id == question_id) and (it.quiz_id == quiz_id) and (it.user_id == user_id)}
    }

    override  fun getUser(user_id: Long): User? {
        return users_list[user_id]
    }

    override  fun insertAllQuestions(vararg multipleChoiceQuestions: MultipleChoiceQuestion) {
        for (question in multipleChoiceQuestions){
            questions[question.question_id] = question
        }
    }

    override  fun insertQuestion(multipleChoiceQuestion: MultipleChoiceQuestion) {
        questions[multipleChoiceQuestion.question_id] = multipleChoiceQuestion
    }

    override  fun insertQuiz(quiz: Quiz) {
        return
    }

    override  fun insertResponse(multipleChoiceResponse: MultipleChoiceResponse) {
        response_list[multipleChoiceResponse.response_id] = multipleChoiceResponse
    }

    override  fun insertResponses(vararg respons: MultipleChoiceResponse) {
        for (response in respons){
            insertResponse(response)
        }
    }
}
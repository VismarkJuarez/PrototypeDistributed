package com.example.myapplication.DAOs

import com.example.myapplication.*

interface DataRepository{
    suspend fun getQuestion(id: Long): Question
    suspend fun getQuestions(id_list: List<Long>): List<Question>
    suspend fun insertAllQuestions(vararg questions: Question)
    suspend fun insertQuestion(question: Question)
    suspend fun getResponses(id_list: List<Long>): List<Response>
    suspend fun getResponse(id: Long): Response
    suspend fun getResponsesByQuestionId(question_id: Long): List<Response>
    suspend fun insertResponse(response: Response)
    suspend fun insertResponses(vararg responses: Response)
}

class DataRepositoryImplementation (
    private val question_dao: QuestionDao,
    private val response_dao: ResponseDao

): DataRepository{

    override suspend fun getQuestions(id_list: List<Long>): List<Question>{
        val QuestionList: MutableList<Question> = arrayListOf()
        QuestionList.addAll(question_dao.getQuestions(id_list))
        return QuestionList
    }

    override suspend fun insertAllQuestions(vararg questions: Question) {
        val listOfQuestions = questions.toList()
        question_dao.insert(listOfQuestions)
        }

    override suspend fun insertQuestion(question: Question) {
        question_dao.insert(question)
    }

    override suspend fun getQuestion(id: Long): Question {
        return question_dao.getQuestion(id)
    }

    override suspend fun getResponse(id: Long): Response {
        return response_dao.getResponse(id)
    }

    override suspend fun getResponses(id_list: List<Long>): List<Response> {
        val ResponseList: MutableList<Response> = arrayListOf()
        ResponseList.addAll(response_dao.getResponses(id_list))
        return ResponseList
    }

    override suspend fun getResponsesByQuestionId(question_id: Long): List<Response> {
        val ResponseList: MutableList<Response> = arrayListOf()
        ResponseList.addAll(response_dao.getResponsesByQuestionID(question_id))
        return ResponseList
    }

    override suspend fun insertResponse(response: Response) {
        response_dao.insert(response)
    }

    override suspend fun insertResponses(vararg responses: Response) {
        response_dao.insert(responses.toList())
    }


    // Responses objects should be inserted one at a time as they're received? Probably not. They should be cached in ram.
    // Then periodically(especially during low load, they should flush to the DB from a Shared Queue.
    // Queue should be getting responses from users -> Let's try to allow it to be distributed(USe interfaces and Dagger DI)
    // Periodically, Queue should flush to DB(at say 15 -> Take 15 from Queue) flush it to DB(transaction).
    // We can also do some time testing and comparing it to processing one response at a time(probably really slow and time prohibitive
    // since a new transaction has to happen each time(and it's a single resource).
}
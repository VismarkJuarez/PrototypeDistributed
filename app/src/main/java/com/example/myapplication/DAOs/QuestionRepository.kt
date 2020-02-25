package com.example.myapplication.DAOs

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.*

interface QuestionRepository{
    suspend fun getQuestion(id: Long): MutableLiveData<MultipleChoiceQuestion>
    suspend fun getQuestions(id_list: List<Long>): MutableLiveData<List<MultipleChoiceQuestion>>
    suspend fun insertAllQuestions(vararg multipleChoiceQuestions: MultipleChoiceQuestion)
    suspend fun insertQuestion(multipleChoiceQuestion: MultipleChoiceQuestion)
    suspend fun getResponses(id_list: List<Long>): MutableLiveData<List<MultipleChoiceResponse>>
    suspend fun getResponse(id: Long): MutableLiveData<MultipleChoiceResponse>
    suspend fun getResponsesByQuestionId(question_id: Long): MutableLiveData<List<MultipleChoiceResponse>>
    suspend fun insertResponse(multipleChoiceResponse: MultipleChoiceResponse)
    suspend fun insertResponses(vararg respons: MultipleChoiceResponse)
}

class QuestionRepositoryImpl (
    private val question_dao: QuestionDao,
    private val response_dao: ResponseDao

): QuestionRepository{

    override suspend fun getQuestions(id_list: List<Long>): MutableLiveData<List<MultipleChoiceQuestion>>{
        return question_dao.getQuestions(id_list)
    }

    override suspend fun insertAllQuestions(vararg multipleChoiceQuestions: MultipleChoiceQuestion) {
        val listOfQuestions = multipleChoiceQuestions.toList()
        question_dao.insert(listOfQuestions)
        }

    override suspend fun insertQuestion(multipleChoiceQuestion: MultipleChoiceQuestion) {
        question_dao.insert(multipleChoiceQuestion)
    }

    override suspend fun getQuestion(id: Long): MutableLiveData<MultipleChoiceQuestion> {
        return question_dao.getQuestion(id)
    }

    override suspend fun getResponse(id: Long): MutableLiveData<MultipleChoiceResponse> {
        return response_dao.getResponse(id)
    }

    override suspend fun getResponses(id_list: List<Long>): MutableLiveData<List<MultipleChoiceResponse>> {
        return response_dao.getResponses(id_list)
    }

    override suspend fun getResponsesByQuestionId(question_id: Long): MutableLiveData<List<MultipleChoiceResponse>> {
        return response_dao.getResponsesByQuestionID(question_id)
    }

    override suspend fun insertResponse(multipleChoiceResponse: MultipleChoiceResponse) {
        response_dao.insert(multipleChoiceResponse)
    }

    override suspend fun insertResponses(vararg respons: MultipleChoiceResponse) {
        response_dao.insert(respons.toList())
    }


    // Responses objects should be inserted one at a time as they're received? Probably not. They should be cached in ram.
    // Then periodically(especially during low load, they should flush to the DB from a Shared Queue.
    // Queue should be getting responses from users -> Let's try to allow it to be distributed(USe interfaces and Dagger DI)
    // Periodically, Queue should flush to DB(at say 15 -> Take 15 from Queue) flush it to DB(transaction).
    // We can also do some time testing and comparing it to processing one response at a time(probably really slow and time prohibitive
    // since a new transaction has to happen each time(and it's a single resource).
}
package com.example.myapplication.DAOs

import com.example.myapplication.*

interface QuestionRepository{
    suspend fun getQuestions(id_list: List<Long>): List<QuestionData>
    suspend fun insertAllQuestions(vararg questions: List<QuestionData>)
    suspend fun insertQuestion(question: QuestionData)
}

class QuestionRepositoryImpl (
    private val multiple_choice_question_dao: MultipleChoiceQuestionDao,
    private val matching_question_dao: MatchingQuestionDao,
    private val fillInTheBlankDao: FillInTheBlankDao,
    private val shortAnswerDao: ShortAnswerDao
): QuestionRepository{
    override suspend fun getQuestions(id_list: List<Long>): List<QuestionData>{
        val QuestionList: MutableList<QuestionData> = arrayListOf()
        QuestionList.addAll(multiple_choice_question_dao.getQuestions(id_list))
        QuestionList.addAll(matching_question_dao.getQuestions(id_list))
        QuestionList.addAll(fillInTheBlankDao.getQuestions(id_list))
        QuestionList.addAll(shortAnswerDao.getQuestions(id_list))
        return QuestionList
    }

    override suspend fun insertAllQuestions(vararg questions: List<QuestionData>) {
        multiple_choice_question_dao.insertAllQuestions(questions)
    }
}
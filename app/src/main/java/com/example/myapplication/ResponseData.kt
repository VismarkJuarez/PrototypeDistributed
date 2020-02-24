package com.example.myapplication

import com.google.gson.Gson

abstract class ResponseData {
    companion object {
        fun createResponse(type: String, json: String): ResponseData {
            val gson = Gson()
            val question: ResponseData
            when (type) {
                "multiple_choice" -> question =
                    gson.fromJson(json, MultipleChoiceResponse::class.java)
                "short_answer" -> question =
                    gson.fromJson(json, ShortAnswerResponse::class.java)
                "fill_in_the_blank" -> question =
                    gson.fromJson(json, FillInTheBlankResponse::class.java)
                "matching" -> question =
                    gson.fromJson(json, MatchingResponse::class.java)
                else -> {
                    throw IllegalArgumentException()
                }
            }
            return question
        }
    }
}
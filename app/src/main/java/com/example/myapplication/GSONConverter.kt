package com.example.myapplication

import com.google.gson.Gson

class GSONConverter(val gson: Gson = Gson()){
    // Store the JSON directly is probably more efficient. We might want to do speed tests.
    fun convertToClass(type: String, json_string: String){
        when(type){
            "multiple_choice_question" -> gson.fromJson(json_string, MultipleChoiceQuestion::class.java)
            "multiple_choice_response" -> gson.fromJson(json_string, MultipleChoiceResponse::class.java)
            "user" -> gson.fromJson(json_string, User::class.java)
        }
    }
}
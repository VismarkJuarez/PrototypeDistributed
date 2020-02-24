package com.example.myapplication
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.Expose

@Entity
abstract class QuestionData(@PrimaryKey(autoGenerate = true) val id: Long){
    // "Static" factory method. I love how simple this is. Kotlin is way better than Java.
    companion object {
        val rawData: String = """{
            "kind": "question",
            "object_id": "cac85635-15c2-4f50-827d-60b2270c639a",
            "type": "multiple_choice",
            "prompt": "Who is the best?",
            "choices": [
            "Mike",
            "Domingo"
            ],
            "answer": "Mike"
            }
            """
        val testResponse: String = """
            {
                                "kind": "response",
                                "type": "multiple_choice",
                                "choice": "Mike",
                                "user_id": "1345125",
                                "nickname": "Brian"
                            }
        """.trimIndent()

        fun createQuestion(type: String, json: String): QuestionData {
            val gson = Gson()
            val question: QuestionData
            when (type) {
                "multiple_choice" -> question =
                    gson.fromJson(json, MultipleChoiceQuestion::class.java)
                "short_answer" -> question =
                    gson.fromJson(json, ShortAnswerQuestion::class.java)
                "fill_in_the_blank" -> question =
                    gson.fromJson(json, FillInTheBlankQuestion::class.java)
                "matching" -> question =
                    gson.fromJson(json, MatchingQuestion::class.java)
                else -> {
                    throw IllegalArgumentException()
                }
            }
            return question
        }
    }
}


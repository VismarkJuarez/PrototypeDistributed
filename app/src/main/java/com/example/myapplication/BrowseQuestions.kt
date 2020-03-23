package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.example.myapplication.DAOs.QuizDatabase
import com.example.myapplication.DAOs.RepositoryImpl
import com.example.myapplication.Models.MultipleChoiceQuestion

class BrowseQuestions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_questions)
        val database = QuizDatabase.getDatabase(this)
        val repository = RepositoryImpl(database.questionDao(), database.responseDao(), database.userDao(), database.quizDao())
        val viewButton = findViewById<Button>(R.id.view_questions)
        val exitButton = findViewById<Button>(R.id.return_from_activity)
        val listView = findViewById<ListView>(R.id.question_browser)

        exitButton.setOnClickListener{
            finish()
        }
        viewButton.setOnClickListener {
                Thread(Runnable {
                    val questions = repository.getAllQuestions()
                    val question_as_string = arrayListOf<String>()
                    for (question in questions){
                        val prompt = question.prompt
                        val id = question.question_id
                        val choices = question.choices
                        question_as_string.add("Prompt: $prompt id: $id choices: $choices")
                    }
                    runOnUiThread {
                        val adapter = ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_list_item_1,
                            question_as_string
                        )
                        listView.adapter = adapter
                    }
                }).start()
        }


    }
}

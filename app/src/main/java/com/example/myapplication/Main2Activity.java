package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String text = QuestionData.Companion.getRawData();
        QuestionData questionData = QuestionData.Companion.createQuestion("multiple_choice", text);
        System.out.println(questionData);
    }
}


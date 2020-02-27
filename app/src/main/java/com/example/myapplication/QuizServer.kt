package com.example.myapplication
import io.javalin.Javalin

class QuizServer {
    fun main() {
        val app = Javalin.create().start(5001)
        app.get("/hello_world") { ctx -> ctx.result("Hello World") }
    }
}
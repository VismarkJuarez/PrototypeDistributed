package com.example.myapplication.Networking
import com.example.myapplication.DAOs.Cache
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import com.example.myapplication.Models.*
import com.google.gson.Gson

class QuizServer {
    fun main() {

        val cache = Cache()
        val app = Javalin.create().start(6000)
        val gson = Gson()

        app.routes {
            post("/recordResponse") { ctx ->
                val response = gson.fromJson<MultipleChoiceResponse>(
                    ctx.body(),
                    MultipleChoiceResponse::class.java
                )
                cache.insertResponse(response)
                print(cache.response_list)
                ctx.json(response)
            }
            get("/getResponse/:id") { ctx ->
                val id = ctx.pathParam("id")
                print(id)
                val response = cache.getResponse(id)
                if (response != null) {
                    ctx.json(response)
                }
            }
        }
    }
}
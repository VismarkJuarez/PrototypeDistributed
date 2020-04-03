package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DAOs.Cache
import com.example.myapplication.DAOs.QuizDatabase
import com.example.myapplication.DAOs.RepositoryImpl
import com.example.myapplication.Models.*
import com.example.myapplication.Networking.NetworkInformation
import com.example.myapplication.Networking.UDPClient
import com.example.myapplication.Networking.UDPListener
import com.example.myapplication.Networking.UDPServer
import com.google.gson.Gson
import com.google.zxing.WriterException
import java.util.*
import kotlin.concurrent.schedule


// https://demonuts.com/kotlin-generate-qr-code/ was used for the basis of  QRCode generation and used pretty much all of the code for the QR methods. Great thanks to the authors!
class MainActivity : AppCompatActivity(), UDPListener, HeartBeatListener {
    private var userType:UserType? = null
    private var bitmap: Bitmap? = null
    private var imageview: ImageView? = null
    private var generateConnectionQrButton: Button? = null
    val converter = GSONConverter()
    var ip = "0.0.0.0"
    val gson = Gson()
    var networkInformation: NetworkInformation? = null
    var activeQuestion: MultipleChoiceQuestion? = null
    val clientOne = NetworkInformation("10.0.2.2", 5000, "client")
    val clientTwo = NetworkInformation("10.0.2.2", 5023, "client")
    val clients = arrayListOf<NetworkInformation>().also{
        it.add(clientOne)
        it.add(clientTwo)
    }

    val clientMonitor = hashMapOf<NetworkInformation, String>().also{
        for (client in clients){
            it.put(client, "green")
        }
    }



    // The in memory object cache!
    private val questionRepo = Cache()

    // The actual persisted database!
    private var repository: RepositoryImpl? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //TODO: print statements are sloppy. Make a logger.
        println("username: " + getIntent().getStringExtra("EXTRA_USER_NAME"))
        println("userType: " + intent.getSerializableExtra("EXTRA_USER_TYPE"))

        imageview = findViewById(R.id.iv)
        generateConnectionQrButton = findViewById(R.id.generate_connection_qr_button)
        val create_question_button = findViewById<Button>(R.id.create_question)
        networkInformation = NetworkInformation.NetworkInfoFactory.getNetworkInfo(this)
        val dataaccess = QuizDatabase.getDatabase(this)
        val answerQuestionButton = findViewById<Button>(R.id.answer_active)
        val responseID = UUID.randomUUID().toString()
        val browseQuestionsButton = findViewById<Button>(R.id.browse_questions)
        repository = RepositoryImpl(dataaccess.questionDao(), dataaccess.responseDao(), dataaccess.userDao(), dataaccess.quizDao())
        Timer("Heartbeat", false).schedule(100, 30000){
          emitHeartBeat()
        }



        val quizId = UUID.randomUUID().toString()

        browseQuestionsButton.setOnClickListener{
            val intent = Intent(this, BrowseQuestions::class.java)
            startActivityForResult(intent, 3)
        }

        answerQuestionButton.setOnClickListener{
            Thread(Runnable {
            val intent = Intent(this, AnswerQuestionActivity::class.java).also{
                if (activeQuestion == null) {
                    runOnUiThread{
                    Toast.makeText(applicationContext,"No active question", Toast.LENGTH_SHORT).show()
                }
                }
                else {
                    val user =
                        User(nickname = "Brian", user_id = "5bca90f1-d5a4-46c5-8394-0b5cebbe1945")
                    val quiz = Quiz(activeQuestion!!.quiz_id, "BobMarley")
                    it.putExtra("active_question", activeQuestion)
                    repository!!.insertUser(user)
                    repository!!.insertQuiz(quiz)
                    it.putExtra("user_id", user.user_id)
                    it.putExtra("response_id", responseID)
                    it.putExtra("quiz_id", quiz.quiz_id)
                }
            }
                startActivityForResult(intent, 2)
            }).start()
        }

        /* We don't want to block the UI thread */
        val server = UDPServer()
        server.addListener(this)
        val udpDataListener = Thread(server)
        if (networkInformation!!.ip == "10.0.2.18"){
            server.setPort(5024)
            println("THIS IS TRUE")
        }
        udpDataListener.start()


        generateConnectionQrButton!!.setOnClickListener {
            try {
                Thread(Runnable {
                    bitmap = QRCodeGenerator.generateInitialConnectionQRCode(500, 500, this)
                    imageview!!.post {
                        imageview!!.setImageBitmap(bitmap)
                    }
                }).start()
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

        create_question_button.setOnClickListener{
            val intent = Intent(this, CreateQuestionActivity::class.java).also{
                it.putExtra("quizID", quizId)
            }
            startActivityForResult(intent, 1)
        }

    }

    override fun onUDP(data: String) {
        Thread(Runnable {
            println(data)
            runOnUiThread {
                Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
            }
            // Debug here. It prints out all questions in the database.
            val type = gson.fromJson(data, Map::class.java)["type"] as String
            val message = converter.convertToClass(type, data)
            println(message)
            if (type == "multiple_choice_question"){
                activeQuestion = message as MultipleChoiceQuestion
                println("Activating a question!")
            }
            if (type == "hb"){
                onHeartBeat(message as HeartBeat)
            }
        }).start()
    }

    private fun emitHeartBeat(){
        println("Emitting heartbeat")
        Thread(Runnable{
            for (client in clients){
                val heartbeat = HeartBeat(ip = networkInformation!!.ip, port = networkInformation!!.port.toString())
                UDPClient().sendMessage(gson.toJson(heartbeat), client.ip, client.port)
            }
        }).start()
    }

    override fun onHeartBeat(heartBeat: HeartBeat) {
        clientMonitor[NetworkInformation(heartBeat.ip, heartBeat.port.toInt(), "client")] = "Yellow"
        println(clientMonitor)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val question = data?.getParcelableExtra("question") as MultipleChoiceQuestion
                questionRepo.insertQuestion(question)
                Thread(Runnable {
                    repository?.insertQuestion(question)
                }).start()

            }
        }
        if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK){
                val response = data?.getParcelableExtra("response") as MultipleChoiceResponse
                val jsonTree = gson.toJsonTree(response).also {
                    it.asJsonObject.addProperty("type", "multiple_choice_response")
                }
                val json = gson.toJson(jsonTree)
                questionRepo.insertResponse(response)
                Thread(Runnable{
                    for (client in clients) {
                        UDPClient().sendMessage(json, client.ip, client.port)
                    }
                }).start()
            }
        }
        if (requestCode == 3){
            if (resultCode == Activity.RESULT_OK){
                val questionToActivate = data?.getParcelableExtra("question") as MultipleChoiceQuestion
                val jsonTree = gson.toJsonTree(questionToActivate).also{
                    it.asJsonObject.addProperty("type", "multiple_choice_question")
                }
                val json = gson.toJson(jsonTree)
                Thread(Runnable{
                    for (client in clients){
                        UDPClient().sendMessage(json, client.ip, client.port)
                    }
                }).start()
            }
        }
    }
}

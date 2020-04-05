package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.*
import com.example.myapplication.DAOs.Cache
import com.example.myapplication.DAOs.QuizDatabase
import com.example.myapplication.DAOs.RepositoryImpl
import com.example.myapplication.Models.*
import com.google.zxing.WriterException
import com.example.myapplication.Networking.*
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.schedule
import java.util.concurrent.ConcurrentHashMap

// https://demonuts.com/kotlin-generate-qr-code/ was used for the basis of  QRCode generation and used pretty much all of the code for the QR methods. Great thanks to the authors!
class MainActivity : AppCompatActivity(), UDPListener, HeartBeatListener {

    val converter = GSONConverter()
    val gson = Gson()
    val debug = true
    var isServer = false
    var networkInformation: NetworkInformation?= null
    var activeQuestion: MultipleChoiceQuestion? = null
    val clientOne = NetworkInformation("10.0.2.2", 5000, "client")
    val clientTwo = NetworkInformation("10.0.2.2", 5023, "server")
    val clientMonitor = ClientMonitor(arrayListOf(clientOne, clientTwo))



    // The in memory object cache!
    private val questionRepo = Cache()

    // The actual persisted database!
    private var repository: RepositoryImpl? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var bitmap: Bitmap? = null
        var imageview = findViewById<ImageView>(R.id.iv)
        val generateConnectionQrButton = findViewById<Button>(R.id.generate_connection_qr_button)
        val create_question_button = findViewById<Button>(R.id.create_question)
        val answerQuestionButton = findViewById<Button>(R.id.answer_active)
        val browseQuestionsButton = findViewById<Button>(R.id.browse_questions)

        browseQuestionsButton.setOnClickListener{
            val intent = Intent(this, BrowseQuestions::class.java)
            startActivityForResult(intent, 3)
        }

        val responseID = UUID.randomUUID().toString()

        answerQuestionButton.setOnClickListener{
            Thread(Runnable {
                Intent(this, AnswerQuestionActivity::class.java).also{
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
                        startActivityForResult(it, 2)
                    }
                }
            }).start()
        }

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

        val quizId = UUID.randomUUID().toString()
        create_question_button.setOnClickListener{
            val intent = Intent(this, CreateQuestionActivity::class.java).also{
                it.putExtra("quizID", quizId)
            }
            startActivityForResult(intent, 1)
        }


        /* We don't want to block the UI thread */
        val server = UDPServer()
        server.addListener(this)
        val udpDataListener = Thread(server)
        networkInformation = NetworkInformation.getNetworkInfo(this)


        if (debug == true) {
            if (networkInformation!!.ip == "10.0.2.18") {
                isServer = true
                server.setPort(5024)
                networkInformation!!.port = 5024
                networkInformation!!.type = "server"
            }
        }
        if (isServer){
            networkInformation!!.type = "server"
        }
        udpDataListener.start()


        val dataaccess = QuizDatabase.getDatabase(this)
        repository = RepositoryImpl(dataaccess.questionDao(), dataaccess.responseDao(), dataaccess.userDao(), dataaccess.quizDao())

        Timer("Heartbeat", false).schedule(100, 5000){
          emitHeartBeat()
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
            if (type == "multiple_choice_question"){
                activeQuestion = message as MultipleChoiceQuestion
                println("Activating a question!")
            }
            if (type == "hb"){
                onHeartBeat(message as HeartBeat)
            }
        }).start()
    }

    // This is the scheduled function. Ideally this can also be packaged with the onHeartBeat etc.
    private fun emitHeartBeat(){
        println("Emitting heartbeat")
        Thread(Runnable{
            for (client in clientMonitor.getClients()) {
                val status = clientMonitor.getClient(client)
                val heartbeat = HeartBeat(
                    ip = networkInformation!!.ip,
                    port = networkInformation!!.port.toString(),
                    peer_type = networkInformation!!.type
                )
                // DEBUG if the heartbeat is going to be sent to the server then actually send it out to "10.0.2.2" Change this to client.ip in prod
                println(clientMonitor)
                if (debug == true) {
                    if (client == clientTwo) {
                        UDPClient().sendMessage(gson.toJson(heartbeat), "10.0.2.2", client.port)
                    }
                }
                else {
                    UDPClient().sendMessage(gson.toJson(heartbeat), client.ip, client.port)
                }
                if (status?.last_received!!.get() == 2) {
                    status.color = "yellow"
                } else if (status.last_received.get() > 2) {
                    if (status.color != "red"){
                        status.color = "red"
                        val data = hashMapOf<String, String>()
                        data.put("type", "failure_detected")
                        val message = gson.toJson(data)
                        for (client in clientMonitor.getClients()){
                            UDPClient().sendMessage(message, client.ip, client.port)
                        }
                        runOnUiThread{
                            Toast.makeText(applicationContext,"Failure detected at $client", Toast.LENGTH_SHORT).show()
                        }
                        }
                }
                status.last_received.getAndIncrement()
            }
        }).start()
    }

    // This is the listener function. It can be packaged together with the clientMonitor functionality.
    override fun onHeartBeat(heartBeat: HeartBeat) {
        println("Heartbeat received!")
        var ip = heartBeat.ip
        var port = heartBeat.port.toInt()
        var peerType = heartBeat.peer_type
        if (debug){
            ip = "10.0.2.2"
            port = 5000
            if (heartBeat.ip == "10.0.2.18"){
                port = 5023
            }
        }
        val client = clientMonitor.getClient(NetworkInformation(ip, port, peerType))


        if (client != null){
            client.color = "green"
            client.last_received.getAndSet(0)
        }
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
                    for (client in clientMonitor.getClients()) {
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
                    for (client in clientMonitor.getClients()){
                        UDPClient().sendMessage(json, client.ip, client.port)
                    }
                }).start()
            }
        }
    }
}

package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.*
import com.example.myapplication.DAOs.Cache
import com.example.myapplication.DAOs.QuizDatabase
import com.example.myapplication.DAOs.RepositoryImpl
import com.example.myapplication.Models.MultipleChoiceQuestion
import com.google.zxing.WriterException
import com.example.myapplication.Models.MultipleChoiceResponse
import com.example.myapplication.Models.Quiz
import com.example.myapplication.Models.User
import com.example.myapplication.Networking.*
import com.google.gson.Gson
import java.util.*

// https://demonuts.com/kotlin-generate-qr-code/ was used for the basis of  QRCode generation and used pretty much all of the code for the QR methods. Great thanks to the authors!
class MainActivity : AppCompatActivity(), UDPListener {
    private var bitmap: Bitmap? = null
    private var imageview: ImageView? = null
    private var generateConnectionQrButton: Button? = null
    val converter = GSONConverter()
    var ip = "0.0.0.0"
    val gson = Gson()
    var networkInformation: NetworkInformation? = null
    var activeQuestion: MultipleChoiceQuestion? = null
    val clientOne = NetworkInformation("10.0.2.2", 5000, "client")
    val clientTwo = NetworkInformation("10.0.2.2", 6000, "client")
    val clients = arrayListOf<NetworkInformation>().also{
        it.add(clientOne)
        it.add(clientTwo)
    }


    // The in memory object cache!
    val questionRepo = Cache()

    // The actual persisted database!
    var repository: RepositoryImpl? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageview = findViewById(R.id.iv)
        generateConnectionQrButton = findViewById(R.id.generate_connection_qr_button)
        val create_question_button = findViewById<Button>(R.id.create_question)
        networkInformation = NetworkInformation.NetworkInfoFactory.getNetworkInfo(this)
        val ipEditor = findViewById<EditText>(R.id.ip_enter)
        val setIP = findViewById<Button>(R.id.set_ip)
        val dataaccess = QuizDatabase.getDatabase(this)
        val answerQuestionButton = findViewById<Button>(R.id.answer_active)
        val responseID = UUID.randomUUID().toString()
        val browseQuestionsButton = findViewById<Button>(R.id.browse_questions)
        repository = RepositoryImpl(dataaccess.questionDao(), dataaccess.responseDao(), dataaccess.userDao(), dataaccess.quizDao())


        val quizId = UUID.randomUUID().toString()

        browseQuestionsButton.setOnClickListener{
            val intent = Intent(this, BrowseQuestions::class.java)
            startActivityForResult(intent, 3)
        }

        setIP.setOnClickListener{
            ip = ipEditor.text.toString()
            Toast.makeText(applicationContext, "Ip is now $ip", Toast.LENGTH_SHORT).show()
        }

        answerQuestionButton.setOnClickListener{
            Thread(Runnable {
            val intent = Intent(this, AnswerQuestionActivity::class.java).also{
                if (activeQuestion == null) {
                    activeQuestion = repository!!.getQuestion("32988dcf-bbc4-4b71-9d51-9dc4ec8b01a6")
                }
                val user = User(nickname="Brian", user_id = "5bca90f1-d5a4-46c5-8394-0b5cebbe1945")
                val quiz = Quiz("5bca90f1-d5a4-46c5-8394-0b5cebbe1946", "BobMarley")
                it.putExtra("active_question", activeQuestion)
                repository!!.insertUser(user)
                repository!!.insertQuiz(quiz)
                it.putExtra("user_id", user.user_id)
                it.putExtra("response_id", responseID)
                it.putExtra("quiz_id", quiz.quiz_id)
            }
                startActivityForResult(intent, 2)
            }).start()
        }

        val udpClient = UDPClient()

        /* We don't want to block the UI thread */
        val server = UDPServer()
        server.addListener(this)
        val udpDataListener = Thread(server)
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
        }).start()
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
                    UDPClient().sendMessage(json, ip,5000)
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
                val client_messenger = UDPClient()
                Thread(Runnable{
                    for (client in clients){
                        client_messenger.sendMessage(json, client.ip, client.port)
                    }
                }).start()
            }
        }
    }
}

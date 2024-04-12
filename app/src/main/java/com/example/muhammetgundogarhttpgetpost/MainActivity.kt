package com.example.muhammetgundogarhttpgetpost

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.muhammetgundogarhttpgetpost.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                performLogin(binding.editTextText.text.toString(),binding.editTextText2.text.toString())
            }

        }







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets



        }
    }


    private suspend fun performLogin(email: String, password: String) {

        val url = URL("https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/login")


        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.doOutput = true

        // JSON verisini oluşturun
        val requestBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }


        val outputStreamWriter = OutputStreamWriter(connection.outputStream)
        outputStreamWriter.write(requestBody.toString())
        outputStreamWriter.flush()
        outputStreamWriter.close()


        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {

            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val response = bufferedReader.readText()






            withContext(Dispatchers.Main) {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                intent.putExtra("token",response)


                startActivity(intent)
                finish()
            }
        } else {

            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_LONG).show()


            }
        }


        connection.disconnect()
    }



}
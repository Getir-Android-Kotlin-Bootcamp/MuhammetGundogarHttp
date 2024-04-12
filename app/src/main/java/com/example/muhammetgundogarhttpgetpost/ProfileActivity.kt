package com.example.muhammetgundogarhttpgetpost

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.muhammetgundogarhttpgetpost.databinding.ActivityProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val token = intent.getStringExtra("token")

        CoroutineScope(Dispatchers.IO).launch {
            fetchProfile(token)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private suspend fun fetchProfile(token: String?) {

        if (token != null) {

            val url = URL("https://espresso-food-delivery-backend-cc3e106e2d34.herokuapp.com/profile/$token")


            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $token")


            val responseCode = connection.responseCode


            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Başarılı yanıt aldıysanız, yanıtı oku
                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val response = bufferedReader.readText()


                val jsonResponse = JSONObject(response)


              //  val name = jsonResponse.getString("name") başka bilgileri de bu şekilde alabiliyoruz
                val email = jsonResponse.getString("email")


                withContext(Dispatchers.Main) {
                    binding.textView.text = "email $email"
                }
            } else {

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProfileActivity,"Error occured",Toast.LENGTH_LONG).show()
                }
            }


            connection.disconnect()
        } else {

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ProfileActivity,"Token is null",Toast.LENGTH_LONG).show()

            }
        }
    }


}
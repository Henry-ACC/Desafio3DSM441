package www.FallenKnowledge.com.sv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddResource : AppCompatActivity() {

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var tipeText: TextView
    private lateinit var urlText: TextView
    private lateinit var imageText: TextView
    private lateinit var AddButton: Button

    var auth_username = "FallenAdmin"
    var auth_password = "adminSU"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_resource)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val data: Bundle? = intent.getExtras()
        if (data != null) {
            auth_username = data.getString("auth_username").toString()
            auth_password = data.getString("auth_password").toString()
        }

        titleText = findViewById(R.id.TitleText)
        descriptionText = findViewById(R.id.DescriptionText)
        imageText = findViewById(R.id.Image)
        urlText = findViewById(R.id.UrlText)
        tipeText = findViewById(R.id.TipeText)
        AddButton = findViewById(R.id.AddButton)

        AddButton.setOnClickListener{
            val title = titleText.text.toString()
            val description = descriptionText.text.toString()
            val tipe = tipeText.text.toString()
            var url = urlText.text.toString()
            var image = imageText.text.toString()

            if (url == "" || url == null){
                url = "https://www.google.com"
            }
            if (image == "" || image == null){
                image = "https://i.postimg.cc/jjsmpHzQ/image.png"
            }

            val resource = Resource(0, title, description, tipe, url, image)
            Log.e("API", "auth_username: $auth_username")
            Log.e("API", "auth_password: $auth_password")

            val retrofit = Retrofit.Builder()
                .baseUrl("https://6708f21daf1a3998ba9fc696.mockapi.io/FallenKnowledge/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(FallenApi::class.java)

            api.addResource(resource).enqueue(object : Callback<Resource> {
                override fun onResponse(call: Call<Resource>, response: Response<Resource>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddResource, "Added succesfully", Toast.LENGTH_SHORT).show()
                        val i = Intent(getBaseContext(), MainActivity::class.java)
                        startActivity(i)
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear alumno: $error")
                        Toast.makeText(this@AddResource, "Error adding resource", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Resource>, t: Throwable) {
                    Toast.makeText(this@AddResource, "Error adding resource", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
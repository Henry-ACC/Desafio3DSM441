package www.FallenKnowledge.com.sv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModifyResource : AppCompatActivity() {

    private lateinit var api: FallenApi
    private var resource: Resource? = null

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var tipeText: TextView
    private lateinit var urlText: TextView
    private lateinit var imageText: TextView
    private lateinit var ModifyButton: Button

    val auth_username = "FallenAdmin"
    val auth_password = "adminSU"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modify_resource)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        titleText = findViewById(R.id.TitleText)
        descriptionText = findViewById(R.id.DescriptionText)
        imageText = findViewById(R.id.Image)
        urlText = findViewById(R.id.UrlText)
        tipeText = findViewById(R.id.TipeText)
        ModifyButton = findViewById(R.id.ModifyButton)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://6708f21daf1a3998ba9fc696.mockapi.io/FallenKnowledge/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FallenApi::class.java)

        val resourceId = intent.getIntExtra("id_resource", -1)
        Log.e("API", "resourceID :  $resourceId")

        var title = intent.getStringExtra("title").toString()
        var description = intent.getStringExtra("description").toString()
        var image = intent.getStringExtra("image").toString()
        var tipe = intent.getStringExtra("tipe").toString()
        var url = intent.getStringExtra("url").toString()

        titleText.setText(title)
        descriptionText.setText(description)
        urlText.setText(url)
        tipeText.setText(tipe)
        imageText.setText(image)

        val resource = Resource(0, title, description, tipe, url, image)

        ModifyButton.setOnClickListener{
            if (resource != null) {
                if (imageText.text.toString() == "" || imageText.text.toString() == null){
                    imageText.setText("https://i.postimg.cc/jjsmpHzQ/image.png")
                }
                if (urlText.text.toString() == "" || urlText.text.toString() == null){
                    urlText.setText("https://www.google.com")
                }

                val resourceModified = Resource(
                    resourceId,
                    titleText.text.toString(),
                    descriptionText.text.toString(),
                    tipeText.text.toString(),
                    urlText.text.toString(),
                    imageText.text.toString()
                )

                val jsonResourceUpdated = Gson().toJson(resourceModified)
                Log.d("API", "JSON sent: $jsonResourceUpdated")

                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                api.modifyResource(resourceId, resourceModified).enqueue(object :
                    Callback<Resource> {
                    override fun onResponse(call: Call<Resource>, response: Response<Resource>) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(this@ModifyResource, "Resource modified succesfully", Toast.LENGTH_SHORT).show()
                            val i = Intent(getBaseContext(), MainActivity::class.java)
                            startActivity(i)
                        } else {
                           try {
                                val errorJson = response.errorBody()?.string()
                                val errorObj = JSONObject(errorJson)
                                val errorMessage = errorObj.getString("message")
                                Toast.makeText(this@ModifyResource, errorMessage, Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                               Toast.makeText(this@ModifyResource, "Error modifying resource", Toast.LENGTH_SHORT).show()
                                Log.e("API", "Error parsing JSON: ${e.message}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<Resource>, t: Throwable) {
                        Log.e("API", "onFailure : $t")
                        Toast.makeText(this@ModifyResource, "Error modifying resource", Toast.LENGTH_SHORT).show()

                        try {
                            val gson = GsonBuilder().setLenient().create()
                            val error = t.message ?: ""
                            val resource = gson.fromJson(error, Resource::class.java)
                        } catch (e: JsonSyntaxException) {
                            Log.e("API", "Error parsing JSON: ${e.message}")
                        } catch (e: IllegalStateException) {
                            Log.e("API", "Error parsing JSON: ${e.message}")
                        }
                    }
                })
            }
        }
    }
}
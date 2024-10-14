package www.FallenKnowledge.com.sv

import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import retrofit2.http.Url

class ShowResource : AppCompatActivity() {

    private lateinit var api: FallenApi
    private var resource: Resource? = null

    private lateinit var TitleText: TextView
    private lateinit var DescriptionText: TextView
    private lateinit var TipeText: TextView
    private lateinit var UrlText: TextView
    private lateinit var Image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_resource)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        TitleText = findViewById(R.id.TitleText)
        DescriptionText = findViewById(R.id.DescriptionText)
        Image = findViewById(R.id.Image)
        UrlText = findViewById(R.id.UrlText)
        TipeText = findViewById(R.id.TipeText)

        val resourceId = intent.getIntExtra("id_resource", -1)
        Log.e("API", "resourceID :  $resourceId")

        var title = intent.getStringExtra("title").toString()
        var description = intent.getStringExtra("description").toString()
        var image = intent.getStringExtra("image").toString()
        var tipe = intent.getStringExtra("tipe").toString()
        var url = intent.getStringExtra("url").toString()

        if (title == null || title == ""){
            title = "Name of the resource not specified"
        }
        if (description == null || description == ""){
            description = "No description"
        }
        if (tipe == null || tipe ==  ""){
            tipe = "No tipe of Resource"
        }

        TitleText.setText(title)
        DescriptionText.setText(description)
        UrlText.setText(url)
        Linkify.addLinks(UrlText, Linkify.WEB_URLS)
        TipeText.setText(tipe)
        Picasso.get().load(image).into(Image)
    }
}
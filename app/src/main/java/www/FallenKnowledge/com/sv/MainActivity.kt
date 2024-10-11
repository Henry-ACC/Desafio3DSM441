package www.FallenKnowledge.com.sv

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResourceAdapter
    private lateinit var api: FallenApi

    val auth_username = "FallenAdmin"
    val auth_password = "adminSU"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val fab_add: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fabAdd)

        recyclerView = findViewById(R.id.rvResources)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://6708f21daf1a3998ba9fc696.mockapi.io/FallenKnowledge/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FallenApi::class.java)

        loadData(api)

        fab_add.setOnClickListener(View.OnClickListener {
            val i = Intent(getBaseContext(), AddResource::class.java)
            i.putExtra("auth_username", auth_username)
            i.putExtra("auth_password", auth_password)
            startActivity(i)
        })
    }

    override fun onResume() {
        super.onResume()
        loadData(api)
    }

    private fun loadData(api: FallenApi) {
        val call = api.obtainResources()
        call.enqueue(object : Callback<List<Resource>> {
            override fun onResponse(call: Call<List<Resource>>, response: Response<List<Resource>>) {
                if (response.isSuccessful) {
                    val resources = response.body()
                    if (resources != null) {
                        adapter = ResourceAdapter(resources)
                        recyclerView.adapter = adapter

                        adapter.setOnItemClickListener(object : ResourceAdapter.OnItemClickListener {
                            override fun onItemClick(resource: Resource) {
                                val opciones = arrayOf("Show Resource", "Modify Resource", "Delete Resource")

                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle(resource.title)
                                    .setItems(opciones) { dialog, index ->
                                        when (index) {
                                            0 -> Show(resource)
                                            1 -> Modify(resource)
                                            2 -> Delete(resource, api)
                                        }
                                    }
                                    .setNegativeButton("Cancel", null)
                                    .show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "There was an error getting resources: $error")
                    Toast.makeText(
                        this@MainActivity,
                        "Problems getting resources 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Resource>>, t: Throwable) {
                Log.e("API", "There was an error getting resources: ${t.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Problems getting resources 1",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun Show(resource: Resource){
        val i = Intent(getBaseContext(), ShowResource::class.java)
        i.putExtra("id_resource", resource.id)
        i.putExtra("titulo", resource.title)
        i.putExtra("descripcion", resource.description)
        i.putExtra("tipo", resource.tipe)
        i.putExtra("enlace", resource.url)
        i.putExtra("imagen", resource.image)
        startActivity(i)
    }

    private fun Modify(resource: Resource) {
        val i = Intent(getBaseContext(), ModifyResource::class.java)
        i.putExtra("id_resource", resource.id)
        i.putExtra("titulo", resource.title)
        i.putExtra("descripcion", resource.description)
        i.putExtra("tipo", resource.tipe)
        i.putExtra("enlace", resource.url)
        i.putExtra("imagen", resource.image)

        startActivity(i)
    }

    private fun Delete(resource: Resource, api: FallenApi) {
        Log.e("API", "id : $resource")
        val call = api.deleteResource(resource.id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Resource deleted", Toast.LENGTH_SHORT).show()
                    loadData(api)
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "There was an error deleting the resource : $error")
                    Toast.makeText(this@MainActivity, "Error deleting resource 1", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "There was an error deleting the resource : $t")
                Toast.makeText(this@MainActivity, "Error deleting resource 2", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
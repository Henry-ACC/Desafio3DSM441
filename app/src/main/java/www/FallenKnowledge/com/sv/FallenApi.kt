package www.FallenKnowledge.com.sv

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FallenApi {

    @GET("Resources/")
    fun obtainResources(): Call<List<Resource>>

    @GET("Resources/{id}")
    fun obtainResourcesById(@Path("id") id: Int): Call<Resource>

    @POST("Resources/")
    fun addResource(@Body resource: Resource): Call<Resource>

    @PUT("Resources/{id}")
    fun modifyResource(@Path("id") id: Int, @Body resource: Resource): Call<Resource>

    @DELETE("Resources/{id}")
    fun deleteResource(@Path("id") id: Int): Call<Void>
}
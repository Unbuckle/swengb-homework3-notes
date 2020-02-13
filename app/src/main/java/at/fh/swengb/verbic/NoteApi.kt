package at.fh.swengb.verbic

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

object NoteApi {
    private const val baseUrl = "https://notes.bloder.xyz"
    private val retrofit: Retrofit
    val retrofitService: NoteApiService
    init {val moshiBuilder = Moshi.Builder().build()
    retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshiBuilder)).baseUrl(
        baseUrl).build()
    retrofitService = retrofit.create(NoteApiService::class.java)}

}
interface NoteApiService {
    @GET("/notes")
    fun notes(@Header("access-token") accessToken: String, @Header("last-sync") lastSync: Long): Call<NotesResponse>
    @POST("/login")
    fun login(@Body body: AuthRequest): Call<AuthResponse>
    @POST("/notes")
    fun addOrUpdateNote(@Header("access-token") accessToken: String, @Body body: Note): Call<Note>
}
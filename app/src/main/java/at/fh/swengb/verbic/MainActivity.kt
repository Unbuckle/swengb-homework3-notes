package at.fh.swengb.verbic

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
companion object{
    const val TOKEN = "TOKEN"
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        if(sharedPreferences.getString(TOKEN,null)== null){
            Log.e("Token","No Token found!")
        }
        else{
            val intent = Intent(this, NoteListActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            if(login_username.text.toString().isNotEmpty() and login_password.text.toString().isNotEmpty()) {
                val authRequest =
                    AuthRequest(login_username.text.toString(), login_password.text.toString())
                login(authRequest,
                    success = {
                        sharedPreferences.edit().putString(TOKEN, it.token).apply()
                        //Log.e("Token", it.token)
                        val intent = Intent(this, NoteListActivity::class.java)
                        startActivity(intent)

                    },
                    error = {
                        Log.e("Error", it)
                    }
                )
            }
            else {
                Toast.makeText(this, this.getString(R.string.login_info_missing_message) , Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login (
        request: AuthRequest,
        success: (response: AuthResponse) -> Unit,
        error: (errorMessage: String) -> Unit) {
        NoteApi.retrofitService.login(request).enqueue(object: retrofit2.Callback<AuthResponse>{
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                error("Failed to log in")
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                }
                else {
                    error("We've got an error")
                }

            }
        })
    }
}
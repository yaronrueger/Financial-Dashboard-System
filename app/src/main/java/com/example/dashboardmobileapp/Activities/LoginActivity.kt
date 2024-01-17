package com.example.dashboardmobileapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dashboardmobileapp.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private var userEdt: EditText? = null
    private var passEdt: EditText? = null
    private var loginBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        setLogIn()
    }

    private fun initView() {
        userEdt = findViewById(R.id.personEdit)
        passEdt = findViewById(R.id.passwordEdit)
        loginBtn = findViewById(R.id.loginBtn)
    }

    private fun getToken(username: String, password: String): String {
        return "grant_type=&username=$username&password=$password&scope=&client_id=&client_secret="
    }

    private fun setLogIn() {
        loginBtn!!.setOnClickListener {
            if (userEdt!!.text.toString().isEmpty() || passEdt!!.text.toString().isEmpty()) {
                Toast.makeText(this@LoginActivity, "Please fill the login form", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //START API
                // Instantiate the RequestQueue.
                val queue = Volley.newRequestQueue(this@LoginActivity)
                val url = "https://YOUR-DOMAIN/login"
                // Request a string response from the provided URL.
                val stringRequest: StringRequest = object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        if (response != null) {
                            try {
                                // get token
                                val jsonObject = JSONObject(response)
                                val accessToken = jsonObject.getString("access_token")
                                //store token
                                val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                val editor = preferences.edit()
                                editor.putString("token", accessToken)
                                editor.putString("username", userEdt!!.text.toString())
                                editor.apply()
                                //start Main
                                refresh()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                passEdt!!.setText("")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        } else {
                        }
                    }, Response.ErrorListener {
                        passEdt!!.setText("")
                        Toast.makeText(
                            this@LoginActivity,
                            "Wrong username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    // Override getBody to include payload
                    override fun getBody(): ByteArray {
                        return getToken(
                            userEdt!!.text.toString(),
                            passEdt!!.text.toString()
                        ).toByteArray()
                    }
                }
                // Add the request to the RequestQueue.
                queue.add(stringRequest)
                //END API
            }
        }
    }

    private fun refresh() {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        //Start API
        // Instantiate the RequestQueue
        val queue = Volley.newRequestQueue(this@LoginActivity)
        val url = "https://YOUR-DOMAIN/refresh"
        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener<String?> { response ->
                if (response != null) {
                    try {
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                }
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + preferences.getString("token", "")
                return headers
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        //End API
    }

}
package com.vinesh.foodie.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vinesh.foodie.R
import com.vinesh.foodie.fragments.ForgotPassword
import com.vinesh.foodie.fragments.Otp
import com.vinesh.foodie.fragments.RegisterFragment
import com.vinesh.foodie.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

lateinit var etMobileNumber: EditText
lateinit var etPassword: EditText
lateinit var btnLogin: Button
lateinit var txtForgotPassword: TextView
lateinit var txtRegisterNow: TextView
lateinit var logInProgressBar: ProgressBar
lateinit var loginToolbar: Toolbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        etMobileNumber = findViewById(R.id.etxtMobileNumber)
        etPassword = findViewById(R.id.etxtPassword)
        btnLogin = findViewById(R.id.btnLogIn)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegisterNow = findViewById(R.id.txtRegisterNow)
        loginToolbar = findViewById(R.id.logInToolbar)
        logInProgressBar = findViewById(R.id.logInProgressBar)
        logInProgressBar.visibility = View.GONE

        setSupportActionBar(loginToolbar)
        supportActionBar?.title = "Log in"

        val LoginQueue = Volley.newRequestQueue(this@LoginActivity)
        val Loginurl = "http://13.235.250.119/v2/login/fetch_result"

        btnLogin.setOnClickListener {

            logInProgressBar.visibility = View.VISIBLE

            if(ConnectionManager().checkConnectivity(this@LoginActivity as Context)){

                val mobileNumber = etMobileNumber.text.toString()
                val Password = etPassword.text.toString()

                val LoginjsonParams = JSONObject()
                LoginjsonParams.put("mobile_number",mobileNumber)
                LoginjsonParams.put("password",Password)

                val jsonObjectRequest = object :JsonObjectRequest(Method.POST,Loginurl,LoginjsonParams, Response.Listener {

                    try{
                        val dataObject = it.getJSONObject("data")
                        val success = dataObject.getBoolean("success")
                        if(success){
                            val data = dataObject.getJSONObject("data")
                            sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
                            sharedPreferences.edit().putString("user_name",data.getString("name")).apply()
                            sharedPreferences.edit().putString("user_email",data.getString("email")).apply()
                            sharedPreferences.edit().putString("user_number",data.getString("mobile_number")).apply()
                            sharedPreferences.edit().putString("user_address",data.getString("address")).apply()
                            sharedPreferences.edit().putBoolean("Logged_in",true).apply()

                            logInProgressBar.visibility = View.GONE

                            val intent = Intent(this@LoginActivity,MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            logInProgressBar.visibility = View.GONE
                            Toast.makeText(this@LoginActivity,it.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                        }
                    }catch(e:JSONException){
                        println(e)
                        Toast.makeText(this@LoginActivity,"Some unexpected Error occurred",Toast.LENGTH_SHORT).show()
                    }


                },Response.ErrorListener {
                    logInProgressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity,"Some unexpected Error Occurred",Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers =  HashMap<String,String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "058cab00f48bff"
                        return headers
                    }

                }
                LoginQueue.add(jsonObjectRequest)

            }
            else{
                logInProgressBar.visibility = View.GONE
                val alertDialog = AlertDialog.Builder(this@LoginActivity as Context,R.style.MyDialogTheme)
                alertDialog.setTitle("Error")
                alertDialog.setMessage("Internet Connection Not Found")
                alertDialog.setPositiveButton("Open Settings"){ textColor, listener ->

                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()

                }
                alertDialog.setNegativeButton("Exit"){text,listener ->

                    ActivityCompat.finishAffinity(this@LoginActivity as Activity)

                }
                alertDialog.create()
                alertDialog.show()
            }
        }
        txtForgotPassword.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginFrame,ForgotPassword())
                .commit()
            supportActionBar?.title = "Reset Password"
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        txtRegisterNow.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginFrame,RegisterFragment())
                .commit()
            supportActionBar?.title = "Register"
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.loginFrame)
        val intent = Intent(this@LoginActivity,LoginActivity::class.java)

        when(frag){

            is ForgotPassword -> {
                startActivity(intent)
            }
            is RegisterFragment -> {
                startActivity(intent)
            }
            is Otp -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.loginFrame,ForgotPassword())
                    .commit()
            }
            else -> ActivityCompat.finishAffinity(this@LoginActivity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val frag = supportFragmentManager.findFragmentById(R.id.loginFrame)
        val intent = Intent(this@LoginActivity,LoginActivity::class.java)

        when(frag){

            is ForgotPassword -> {
                startActivity(intent)
            }
            is RegisterFragment -> {
                startActivity(intent)
            }
            is Otp -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.loginFrame,ForgotPassword())
                    .commit()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}
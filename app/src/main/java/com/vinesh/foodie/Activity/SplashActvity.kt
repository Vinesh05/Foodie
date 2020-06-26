package com.vinesh.foodie.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.vinesh.foodie.R

class SplashActvity : AppCompatActivity() {

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_actvity)

        handler = Handler()
        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        val intent:Intent

        if(sharedPreferences.getBoolean("Logged_in",false)){
            intent = Intent(this,MainActivity::class.java)
        }
        else{
            intent = Intent(this,LoginActivity::class.java)
        }

        handler.postDelayed({
            startActivity(intent)
        },1000)

    }
}
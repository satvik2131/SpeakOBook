package com.example.speakobook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule


//LOGO SCREEN
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        supportActionBar?.hide();


        Timer("LoadLogo", false).schedule(500) {
            var moveToAddBooks = Intent(applicationContext,AddBooks::class.java);
            startActivity(moveToAddBooks);
            finish()
        }
    }
}
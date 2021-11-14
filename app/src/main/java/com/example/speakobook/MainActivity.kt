package com.example.speakobook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
//        Handler().postDelayed({
//                                var moveToAddBooks = Intent(applicationContext,AddBooks::class.java);
//                                startActivity(moveToAddBooks);
//        },2000)

        Timer("LoadLogo", false).schedule(500) {
            var moveToAddBooks = Intent(applicationContext,AddBooks::class.java);
            startActivity(moveToAddBooks);
        }
    }
}
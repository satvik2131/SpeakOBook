package com.example.speakobook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AddBooks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_books)
    }

    fun openFileManager(view: View) {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select Your Book"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Started", "hhhhhhhh");
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == RESULT_OK){
            val selectedFile = data?.data
            Log.d("FILE", selectedFile.toString());
        }
    }
}
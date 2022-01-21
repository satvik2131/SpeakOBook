package com.example.speakobook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate




class AddBooks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_books)
        supportActionBar?.hide();

    }

    fun openFileManager(view: View) {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select Your Book"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)

            if(requestCode == 111 && resultCode == RESULT_OK){

                var inpUri: Uri? = data?.data!!

//                Move TO Pdf reader section
                var moveTOReadingSegment:Intent = Intent(this,Reader::class.java).also {
                    it.putExtra("pdfUri",inpUri.toString());
                };

                startActivity(moveTOReadingSegment);

            }
        }catch (e:Exception){
            Log.d("Error",e.message.toString());
        }
    }


}
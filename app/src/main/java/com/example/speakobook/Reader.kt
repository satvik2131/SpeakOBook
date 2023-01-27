package com.example.speakobook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MotionEventCompat
import com.github.barteksc.pdfviewer.PDFView
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import jp.wasabeef.blurry.Blurry
import org.bouncycastle.util.Integers
import kotlin.collections.ArrayList

class Reader : AppCompatActivity(){
    var pageNo:Int = 0;
    var pdfView: PDFView? = null;
    var tts:TextToSpeech? = null;
    var pdfPages = ArrayList<CharSequence>();
    var status:String = "play";
    var pdfDocument:PdfDocument? = null;

    var sharedPreferences: SharedPreferences? =  null;

    //TTS VAR
    var pitch:Float = 0F
    var speech_rate:Float = 0F



    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongViewCast", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reader);
        supportActionBar?.hide();
        //For changing the navigation bar color to theme color
        window.navigationBarColor = getColor(R.color.navBottom);

        sharedPreferences = getSharedPreferences("VOICE_CONFIG", MODE_PRIVATE)

        setSharedOrDefaultPitchAndSpeechRate()



        //Fetches the Uri
        var pagesUriString = intent.getStringExtra("pdfUri");
        val pagesUri = Uri.parse(pagesUriString);

        //Using pdf as it is for layout --> pdf
        pdfView = findViewById<PDFView>(R.id.readerView);
        pdfView?.fromUri(pagesUri)?.pages(pageNo)?.load();


        //For converting text to speech --> Pdf
        //Making Stream with Uri
        var stream = this.contentResolver.openInputStream(pagesUri);

        //Getting PdfReader Instance
        var reader = PdfReader(stream);

        //Storing pdf Pages in array
        var filePages: ArrayList<CharSequence>? = null ;

        //Used for iText 7
        pdfDocument = PdfDocument(reader);

        //Length of pages
        val lenOfBooks: Int = pdfDocument!!.numberOfPages;
        performDataFetch(lenOfBooks,reader);

    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onResume() {
        //Blurring background
        var rootView:ConstraintLayout = findViewById(R.id.readerSetup)
        Blurry.delete(rootView);

        //Sets the global pitch and speech_rate variable
        setSharedOrDefaultPitchAndSpeechRate()


        //Sets the TTS speech and pitch rate
        setPitchAndSpeechRate()

        super.onResume()
    }


    //Swipe Functionality for Voice Configuration
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action: Int = MotionEventCompat.getActionMasked(event);
        var DEBUG_TAG = "hhakuna"

        return when (action) {

            MotionEvent.ACTION_MOVE -> {
                Log.d(DEBUG_TAG, "Action was MOVE")
                true
            }
            MotionEvent.ACTION_UP -> {
                var rootView:ConstraintLayout = findViewById(R.id.readerSetup)

                Blurry.with(this)
                    .radius(10)
                    .sampling(8)
                    .async()
                    .animate(2)
                    .onto(rootView);

                startActivity(Intent(this,PopConfig::class.java));
                true
            }

            else -> super.onTouchEvent(event)

        }
    }

    fun setSharedOrDefaultPitchAndSpeechRate(){
        if((sharedPreferences?.contains("pitch") == true) && (sharedPreferences?.contains("speech_rate") == true)){
            pitch = sharedPreferences?.getFloat("pitch",0F)!!
            speech_rate =  sharedPreferences?.getFloat("speech_rate",0F)!!
        }else{
//            pitch = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.TTS_DEFAULT_PITCH).toFloat())
//            speech_rate = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.TTS_DEFAULT_RATE).toFloat())
        }
    }

    //Fetches data from pdf
    fun performDataFetch(lenOfBooks: Int, reader: PdfReader): ArrayList<CharSequence> {

        //Assignment of pdf pages in array of objects
        for(i in 1 until lenOfBooks){
            var pdfPage = pdfDocument?.getPage(i);

            //Assignment of pdf data to array
            var pdfText = PdfTextExtractor.getTextFromPage(pdfPage).trim();
            pdfPages.add(pdfText);
        }

        return pdfPages;
    }



    //Playing the audio of text
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun playPauseAudio(view: android.view.View) {
        try{

            if(status==="play"){
                var playPause:ImageButton = findViewById(R.id.playPause);
                playPause.setImageResource(R.drawable.pause);

                var readingMessage = pdfPages.get(pageNo);

                tts!!.speak(readingMessage.toString(),TextToSpeech.QUEUE_ADD,null,"readingContent");

                status = "pause";

            }else{
                tts?.stop();
                var playPause:ImageButton = findViewById(R.id.playPause);
                playPause.setImageResource(R.drawable.play);
                status = "play";
            }
        }catch(e:Exception){
            e.message?.let { Log.d("error", it) };
        }

    }


    fun setPitchAndSpeechRate(){
        tts = TextToSpeech(this,TextToSpeech.OnInitListener {
            tts!!.setPitch(pitch!!/100)
            tts!!.setSpeechRate(speech_rate/50)
        });
    }


    //Next Page
    // Taking the string from URI and passing pdf View through which it can directly rendes it
    fun nextPage(view: android.view.View){
        if(status === "pause"){
            Toast.makeText(this,"Ruko Zara Sabar Karo!",Toast.LENGTH_SHORT).show();
        }else{
            var pagesUriString = intent.getStringExtra("pdfUri");
            val pagesUri = Uri.parse(pagesUriString);
            pageNo = pageNo + 1;
            pdfView?.fromUri(pagesUri)?.pages(pageNo)?.load();
        }

    }

    //Previous Page
    fun prevPage(view: android.view.View) {
        if(status === "pause"){
            Toast.makeText(this,"Ruko Zara Sabar Karo!",Toast.LENGTH_SHORT).show();
        }else{
            var pagesUriString = intent.getStringExtra("pdfUri");
            val pagesUri = Uri.parse(pagesUriString);
            pageNo = pageNo - 1;
            pdfView?.fromUri(pagesUri)?.pages(pageNo)?.load();
        }
    }

}



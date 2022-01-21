package com.example.speakobook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import app.minimize.com.seek_bar_compat.SeekBarCompat
import kotlin.math.roundToInt


class PopConfig : AppCompatActivity() {
    var sharedPreferences:SharedPreferences? = null
    private var editor:SharedPreferences.Editor? = null

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popconfig)
        supportActionBar?.hide()

        var dm:DisplayMetrics = DisplayMetrics();
        windowManager.defaultDisplay.getMetrics(dm)
        var width:Int = dm.widthPixels;
        var height:Int = dm.heightPixels;

        window.setLayout((width*.8).toInt(), (height*.5).toInt())
        window.setBackgroundDrawable(getDrawable(R.drawable.popup));


        sharedPreferences = this.getSharedPreferences("VOICE_CONFIG", MODE_PRIVATE);
        editor = sharedPreferences?.edit();

        //Sets the Seekbar data
        setProgressState()


        //Progress Bar Listener
        var pitchAndSpeedListener:SeekBar.OnSeekBarChangeListener = object:SeekBar.OnSeekBarChangeListener{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var pitch:SeekBarCompat = findViewById<SeekBarCompat>(R.id.pitch);
                var speech:SeekBarCompat = findViewById<SeekBarCompat>(R.id.speed_rate);

                var pitchRate:Float = pitch.progress.toFloat();
                var speech_rate:Float = speech.progress.toFloat();

                //********* Shared Data ********
                editor?.putFloat("pitch", pitchRate);
                editor?.putFloat("speech_rate", speech_rate)
                //*******************************

                editor?.apply()
                editor?.commit()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        }

        //Setting the listener to update the pitch and speed in Shared Data
        findViewById<SeekBarCompat>(R.id.speed_rate).setOnSeekBarChangeListener(pitchAndSpeedListener)
        findViewById<SeekBarCompat>(R.id.pitch).setOnSeekBarChangeListener(pitchAndSpeedListener)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setProgressState(){
        var pitch:SeekBarCompat = findViewById<SeekBarCompat>(R.id.pitch);
        var speech:SeekBarCompat = findViewById<SeekBarCompat>(R.id.speed_rate);

        var pitch_value: Int = this.sharedPreferences!!.getFloat("pitch",0F).roundToInt()
        var speech_value: Int = this.sharedPreferences!!.getFloat("speech_rate",0F).roundToInt()

        pitch.setProgress(pitch_value,false)
        speech.setProgress(speech_value,false)
    }
}
package com.example.practice

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practice.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val tag = "MainActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setClownMaskTvOnClick()

        Log.d(tag, "MainActivity is created")
    }

    private fun setClownMaskTvOnClick() {

        mainBinding.clownMaskTv.setOnClickListener {
            val input = mainBinding.namePt.text.toString()
            val textToSet: String = when(
                HashUtils.sha256(input.lowercase())
            ){
                Names.DANCER -> "Oh hey, it's a dancing clown!"
                Names.DED -> "Both punk and clown, bruh..."
                Names.SUGAR -> "Ladies and clowns, the Dev Clown himself!"
                else -> "No clowns here..."
            }

            Log.d(tag, "namePt input: ${input.ifEmpty { "-NO INPUT FROM USER-" }}; textToSet: $textToSet")
            mainBinding.resultTv.visibility = View.VISIBLE
            mainBinding.resultTv.text = textToSet
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "MainActivity is started")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "MainActivity is paused")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "MainActivity is resumed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "MainActivity is destroyed")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "MainActivity is stopped")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "MainActivity is restarted")
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.d(tag, "MainActivity is postResumed")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d(tag, "Huh... low memory...")
    }
}
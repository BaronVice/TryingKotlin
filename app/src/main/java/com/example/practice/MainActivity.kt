package com.example.practice

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import com.example.practice.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val tag = "MainActivityLog"

    private lateinit var handler: Handler
    private lateinit var taskToRun: Runnable
    private var mInterval = 5000

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
        handler = Handler(Looper.myLooper()!!)
        taskToRun = Runnable {
            if (mainBinding.donkeyIv.visibility == View.VISIBLE){
                mainBinding.donkeyIv.visibility = View.INVISIBLE
            } else {
                mainBinding.donkeyIv.updateLayoutParams<MarginLayoutParams> {
                    setMargins( 0, 0, 0, Random.nextInt(
                        // todo: help, refactor
                        0, resources.displayMetrics.run { heightPixels / density }.toInt()-200
                    ))
                }
                mainBinding.donkeyIv.visibility = View.VISIBLE
            }

            val t = Random.nextLong(100, 5000)
            Log.d(tag, "Generated time: $t")

            val res = handler.postDelayed(taskToRun, t)
            Log.d(tag, "Runnable task ${if (res) "successfully executed" else "failed to execute"}")
        }
        startRepeatingTasks()

        Log.d(tag, "MainActivity is created")
    }

    private fun startRepeatingTasks() {
        taskToRun.run()
    }

    private fun setClownMaskTvOnClick() {

        mainBinding.clownMaskTv.setOnClickListener {
            val input = mainBinding.namePt.text.toString()
            val textToSetId: Int = when(
                HashUtils.sha256(input.lowercase())
            ){
                Names.DANCER -> R.string.dancing_clown
                Names.DED -> R.string.ded_clown
                Names.SUGAR -> R.string.dev_clown
                else -> R.string.not_clown
            }

            Log.d(tag, "namePt input: ${input.ifEmpty { "-NO INPUT FROM USER-" }}; textToSet: ${getString(textToSetId)}")
            mainBinding.resultTv.visibility = View.VISIBLE
            mainBinding.resultTv.text = getString(textToSetId)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "MainActivity is started")
    }

    override fun onPause() {
        super.onPause()
        stopRepeatingTask()
        Log.d(tag, "MainActivity is paused")
    }

    override fun onResume() {
        super.onResume()
        startRepeatingTasks()
        Log.d(tag, "MainActivity is resumed")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingTask()
        Log.d(tag, "MainActivity is destroyed")
    }

    override fun onStop() {
        super.onStop()
        stopRepeatingTask()
        Log.d(tag, "MainActivity is stopped")
    }

    private fun stopRepeatingTask() {
        handler.removeCallbacks(taskToRun)
    }

    override fun onRestart() {
        super.onRestart()
        startRepeatingTasks()
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
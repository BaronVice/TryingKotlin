package com.example.practice

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import com.example.practice.databinding.ActivityMainBinding
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val tag = "MainActivityLog"
    private val donkeyTap = AtomicInteger(1)

    private lateinit var handler: Handler
    private lateinit var taskToRun: Runnable
    private var mInterval = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(mainBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            Log.d(tag, result.resultCode.toString())
            if (result.resultCode == RESULT_OK){
                val d: Intent? = result.data
                val isResurrected = d?.getBooleanExtra("resurrect", false)
                if (isResurrected != null && isResurrected){
                    donkeyTap.set(1)
                    startRepeatingTasks()
                }
            }
        }

        setClownMaskTvOnClick()
        handler = Handler(Looper.myLooper()!!)
        taskToRun = Runnable { fancyDonkeys() }
        startRepeatingTasks()

        Log.d(tag, "MainActivity is created")
    }

    private fun fancyDonkeys() {
        val left = mainBinding.donkeyIvReverse
        val right = mainBinding.donkeyIv

        if (left.visibility == View.VISIBLE || right.visibility == View.VISIBLE){
            donkeyAway()
        } else {
            val toShow = if (Random.nextBoolean()) left else right
            toShow.updateLayoutParams<MarginLayoutParams> {
                setMargins( 0, 0, 0, Random.nextInt(
                    // todo: help, refactor
                    0, resources.displayMetrics.run { heightPixels / density }.toInt()
                ))
            }
            toShow.visibility = View.VISIBLE
        }

        val t = Random.nextLong(100, 5000)
        Log.d(tag, "Generated time: $t")

        val res = handler.postDelayed(taskToRun, t)
        Log.d(tag, "Runnable task ${if (res) "successfully executed" else "failed to execute"}")
    }

    private fun donkeyAway() {
        mainBinding.donkeyIv.visibility = View.GONE
        mainBinding.donkeyIvReverse.visibility = View.GONE
    }

    fun donkeyOnTap(v: View){
        v.visibility = View.INVISIBLE
        if(donkeyTap.incrementAndGet() > 10){
            stopRepeatingTask()
            donkeyAway()
            mainBinding.resultTv.text = getString(R.string.donkey_is_dead)
            mainBinding.resultTv.visibility = View.VISIBLE
        }
    }

    private fun startRepeatingTasks() {
        if (donkeyTap.get() < 10) taskToRun.run()
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
                Names.AMOGUS -> {
                    val anotherActivity = Intent(this, AnotherActivity::class.java)
                    anotherActivity.putExtra("isKilled", donkeyTap.get() >= 10)
                    resultLauncher.launch(anotherActivity); R.string.transition
                }
                else -> { if (donkeyTap.get() >= 10) R.string.nothing else R.string.not_clown }
            }

            Log.d(tag, "namePt input: ${input.ifEmpty { "-NO INPUT FROM USER-" }}; textToSet: ${getString(textToSetId)}")

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
        mainBinding.resultTv.text = ""
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
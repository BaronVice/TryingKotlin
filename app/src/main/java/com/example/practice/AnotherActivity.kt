package com.example.practice

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practice.databinding.ActivityAnotherBinding

class AnotherActivity : AppCompatActivity() {
    private lateinit var anotherBinding: ActivityAnotherBinding
    private val tag = "MainActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anotherBinding = ActivityAnotherBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(anotherBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isKilled = intent.getBooleanExtra("isKilled", false)
        handleIsKilled(isKilled)
    }

    private fun handleIsKilled(killed: Boolean) {
        anotherBinding.sadBtn.visibility = View.VISIBLE

        if (killed){
            anotherBinding.anotherTV.text = getString(R.string.deadDonkeyMessage)
            anotherBinding.resurrectBtn.visibility = View.VISIBLE
        }
        else {
            anotherBinding.anotherTV.text = getString(R.string.nothingToSee)
        }
    }

    fun onSadBtnTap(v: View) {
        anotherBinding.sadBtn.visibility = View.INVISIBLE
        anotherBinding.resurrectBtn.visibility = View.INVISIBLE

        intent.putExtra("resurrect", false)
        setResult(RESULT_OK, intent)
        finish()
    }

    fun onResurrectBtnTap(view: View) {
        anotherBinding.sadBtn.visibility = View.INVISIBLE
        anotherBinding.resurrectBtn.visibility = View.INVISIBLE

        anotherBinding.anotherTV.text = getString(R.string.happyFace)
        intent.putExtra("resurrect", true)
        setResult(RESULT_OK, intent)
        finish()
    }
}
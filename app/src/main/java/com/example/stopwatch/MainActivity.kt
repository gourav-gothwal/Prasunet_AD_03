package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var startTime = 0L
    private var timeInMillis = 0L
    private var timeSwapBuff = 0L
    private var updateTime = 0L
    private val handler = Handler()

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMillis = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMillis

            val secs = (updateTime / 1000).toInt()
            val mins = secs / 60
            val milliseconds = (updateTime % 1000).toInt()
            val displaySecs = secs % 60

            binding.tvTimer.text = String.format("%02d:%02d:%03d", mins, displaySecs, milliseconds)

            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(updateTimerThread, 0)
            binding.btnStart.visibility = View.GONE
            binding.btnPause.visibility = View.VISIBLE
        }

        binding.btnPause.setOnClickListener {
            timeSwapBuff += timeInMillis
            handler.removeCallbacks(updateTimerThread)
            binding.btnStart.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE
        }

        binding.btnReset.setOnClickListener {
            startTime = 0L
            timeInMillis = 0L
            timeSwapBuff = 0L
            updateTime = 0L
            binding.tvTimer.text = "00:00:000"
            handler.removeCallbacks(updateTimerThread)
            binding.btnStart.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE
        }
    }
}

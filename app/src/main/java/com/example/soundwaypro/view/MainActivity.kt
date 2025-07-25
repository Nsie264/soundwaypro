package com.example.soundwaypro.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.soundwaypro.R
import com.example.soundwaypro.model.Song
import com.example.soundwaypro.presenter.MusicPresenter
import com.example.soundwaypro.presenter.MusicPresenterImpl
import com.example.soundwaypro.service.MusicService


class MainActivity : AppCompatActivity() {

    private lateinit var presenter: MusicPresenter
    private lateinit var listView: ListView
    private lateinit var playPauseBtn: ImageView
    private lateinit var seekBar: SeekBar
    private val handler = Handler(Looper.getMainLooper())

    private val updateRunnable = object : Runnable {
        override fun run() {
            MusicService.mediaPlayer?.let { player ->
                seekBar.max = player.duration
                seekBar.progress = player.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
    }

    private val playStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.soundwaypro.PLAY_STATE_CHANGED") {
                val isPlaying = intent.getBooleanExtra("isPlaying", false)
                updatePlayPauseIcon(isPlaying)
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MusicPresenterImpl(this)

        listView = findViewById(R.id.songListView)
        playPauseBtn = findViewById(R.id.btnPlayPause)
        seekBar = findViewById(R.id.seekBar)

        val filter = IntentFilter("com.example.soundwaypro.PLAY_STATE_CHANGED")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // API 33 trở lên (Android 13+), cần flag rõ ràng
            registerReceiver(playStateReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            // API 32 trở xuống, dùng bản cũ không cần flag
            registerReceiver(playStateReceiver, filter)
        }


        // SeekBar change listener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicService.mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Songs list với tên mới
        val songs = listOf(
            Song("Stellar Dreams", "Luna Echo", R.raw.lauv),
            Song("Midnight Whispers", "Neon Shadows", R.raw.lonely)
        )

        val adapter = com.example.soundwaypro.adapter.SongAdapter(this, songs)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            presenter.onSongSelected(songs[position].resId)
            handler.post(updateRunnable)
        }

        playPauseBtn.setOnClickListener {
            presenter.togglePlayPause()
        }
    }

    private fun updatePlayPauseIcon(isPlaying: Boolean) {
        if (isPlaying) {
            playPauseBtn.setImageResource(android.R.drawable.ic_media_pause)
        } else {
            playPauseBtn.setImageResource(android.R.drawable.ic_media_play)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
        unregisterReceiver(playStateReceiver)
    }
}

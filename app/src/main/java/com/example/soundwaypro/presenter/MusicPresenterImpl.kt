package com.example.soundwaypro.presenter

import android.content.Context
import android.content.Intent
import com.example.soundwaypro.service.MusicService

class MusicPresenterImpl(private val context: Context) : MusicPresenter {

    override fun onSongSelected(resId: Int) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "ACTION_PLAY"
            putExtra("resId", resId)
        }
        context.startService(intent)
    }

    override fun togglePlayPause() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "ACTION_TOGGLE"
        }
        context.startService(intent)
    }
}

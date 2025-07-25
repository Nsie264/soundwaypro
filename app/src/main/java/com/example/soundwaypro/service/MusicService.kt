package com.example.soundwaypro.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.soundwaypro.utils.NotificationUtil

class MusicService : Service() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    private var isPlaying = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {

            "ACTION_PLAY" -> {
                val resId = intent.getIntExtra("resId", -1)
                if (resId != -1) {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(this, resId)
                    mediaPlayer?.start()
                    isPlaying = true
                    showNotification()
                    broadcastPlayState()
                }
            }

            "ACTION_TOGGLE" -> {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        it.pause()
                        isPlaying = false
                    } else {
                        it.start()
                        isPlaying = true
                    }
                    showNotification()
                    broadcastPlayState()
                }
            }
        }
        return START_STICKY
    }

    private fun showNotification() {
        val notification = NotificationUtil.createMusicNotification(this)
        startForeground(1, notification)
    }

    private fun broadcastPlayState() {
        val intent = Intent("com.example.soundwaypro.PLAY_STATE_CHANGED")
        intent.putExtra("isPlaying", isPlaying)
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

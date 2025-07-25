package com.example.soundwaypro.presenter

interface MusicPresenter {
    fun onSongSelected(resId: Int)
    fun togglePlayPause()
}
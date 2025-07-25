package com.example.soundwaypro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.soundwaypro.R
import com.example.soundwaypro.model.Song

class SongAdapter(
    private val context: Context,
    private val songs: List<Song>
) : BaseAdapter() {

    override fun getCount(): Int = songs.size

    override fun getItem(position: Int): Any = songs[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_song, parent, false)

        val title = view.findViewById<TextView>(R.id.songTitle)
        val artist = view.findViewById<TextView>(R.id.songArtist)
        val icon = view.findViewById<ImageView>(R.id.songIcon)

        val song = songs[position]
        title.text = song.title
        artist.text = song.artist
        // Sử dụng icon nhạc tùy chỉnh
        icon.setImageResource(R.drawable.ic_music_note)

        return view
    }
}

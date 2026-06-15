package com.example.assignment62

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object MusicUtils {

    fun getAudioFiles(context: Context): List<Song> {
        val songList = mutableListOf<Song>()

        // 1. Khai báo các cột dữ liệu muốn lấy từ database hệ thống
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )

        // 2. Chạy câu lệnh truy vấn (Query) để tìm các file nhạc
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, // Không có điều kiện lọc (Lấy tất cả)
            null,
            "${MediaStore.Audio.Media.TITLE} ASC" // Sắp xếp theo A-Z của tiêu đề
        )

        // 3. Đọc dữ liệu từ kết quả truy vấn
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn) ?: "Unknown Artist"

                // Nối ID bài hát vào URI gốc của hệ thống MediaStore
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Lưu contentUri.toString() vào thay vì đường dẫn vật lý
                songList.add(Song(id, title, artist, contentUri.toString()))
            }
        }
        return songList
    }
}
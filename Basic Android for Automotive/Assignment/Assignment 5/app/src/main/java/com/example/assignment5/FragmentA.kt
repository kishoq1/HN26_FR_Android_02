package com.example.assignment5

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentA : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitle = view.findViewById<TextView>(R.id.tvFragmentTitle)
        tvTitle.text = "Danh sách Ca sĩ"

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val singers = listOf(
            Singer("Mỹ Tâm", listOf(Song("Họa Mi Tóc Nâu", "3:45"), Song("Đừng Hỏi Em", "4:12"))),
            Singer("Sơn Tùng MTP", listOf(Song("Nơi Này Có Anh", "4:20"), Song("Lạc Trôi", "3:50"))),
            Singer("Mỹ Linh", listOf(Song("Hương Ngọc Lan", "5:00"), Song("Tóc Ngắn", "4:10"))),
            Singer("Noo Phước Thịnh", listOf(Song("Gạt Đi Nước Mắt", "4:05"), Song("Chạm Khẽ Tim Anh", "5:15"))),
            Singer("Đức Phúc", listOf(Song("Hơn Cả Yêu", "4:30"), Song("Ánh Nắng Của Anh", "4:15"))),
            Singer("Jack", listOf(Song("Sóng Gió", "4:50"), Song("Bạc Phận", "4:25"))),
            Singer("Đan Trường", listOf(Song("Kiếp Ve Sầu", "4:10"), Song("Đi Về Nơi Xa", "3:55"))),
            Singer("Erik", listOf(Song("Chạm Đáy Nỗi Đau", "4:20"), Song("Em Không Sai Chúng Ta Sai", "4:40"))),
            Singer("Trung Quân", listOf(Song("Dấu Mưa", "4:12"), Song("Trót Yêu", "4:35")))
        )

        val adapter = SingerAdapter(singers) { selectedSinger ->
            val fragmentB = FragmentB.newInstance(selectedSinger)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentB)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter
    }
}
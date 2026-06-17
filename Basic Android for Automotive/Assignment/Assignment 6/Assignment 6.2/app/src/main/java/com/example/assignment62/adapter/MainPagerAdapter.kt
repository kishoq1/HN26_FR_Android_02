package com.example.assignment62.adapter


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignment62.ui.AlbumsFragment
import com.example.assignment62.ui.ArtistsFragment
import com.example.assignment62.ui.PlaylistsFragment

class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    // Khai báo ứng dụng của chúng ta có tổng cộng 3 tab
    override fun getItemCount(): Int {
        return 3
    }

    // Trả về Fragment tương ứng với vị trí (position) mà người dùng đang vuốt tới
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlaylistsFragment()
            1 -> ArtistsFragment()
            2 -> AlbumsFragment()
            else -> PlaylistsFragment() // Trạng thái mặc định dự phòng
        }
    }
}
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

class FragmentB : Fragment() {

    private var singer: Singer? = null

    companion object {
        private const val ARG_SINGER = "singer_data"

        fun newInstance(singer: Singer): FragmentB {
            val fragment = FragmentB()
            val args = Bundle()
            args.putSerializable(ARG_SINGER, singer)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singer = arguments?.getSerializable(ARG_SINGER) as? Singer
    }

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        singer?.let {
            tvTitle.text = "Bài hát của ${it.name}"
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = SongAdapter(it.songs)
        }
    }
}
package com.example.search_media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.search_media.databinding.FragmentSearchBinding
import com.example.search_media.list.ListAdapter

class SearchFragment : Fragment() {
    // null 로 설정 하므로서 메모리 누수 방지
    private var binding : FragmentSearchBinding? = null

    private val adapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSearchBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // null 로 만들어 주면서 메모리 누수를 방지 시킴
        binding = null
    }
}
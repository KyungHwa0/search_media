package com.example.search_media

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.search_media.databinding.FragmentSearchResultBinding
import com.example.search_media.list.ItemHandler
import com.example.search_media.list.ListAdapter
import com.example.search_media.model.ListItem
import com.example.search_media.repository.SearchRepositoryImpl

class SearchFragment : Fragment() {

    private val viewModel : SearchViewModel by viewModels {
        SearchViewModel.SearchViewModelFactory(SearchRepositoryImpl(RetrofitManager.searchService))
    }
    // null 로 설정 하므로서 메모리 누수 방지
    private var binding : FragmentSearchResultBinding? = null

    private val adapter by lazy { ListAdapter(Handler(viewModel)) }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("SEARCH_PREF", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSearchResultBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            recyclerView.adapter = adapter

            val lastSearchQuery = sharedPreferences.getString("LAST_SEARCH_QUERY", "")
        }
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // null 로 만들어 주면서 메모리 누수를 방지 시킴
        binding = null
    }

    fun searchKeyword(text: String) {
        viewModel.search(text)

        // 저장
        with(sharedPreferences.edit()) {
            putString("LAST_SEARCH_QUERY", text)
            apply()
        }
    }

    private fun observeViewModel() {
        viewModel.listLiveData.observe(viewLifecycleOwner) {
            binding?.apply {
                if(it.isEmpty()) {
                    emptyTextView.isVisible = true
                    recyclerView.isVisible = false
                } else {
                    emptyTextView.isVisible = false
                    recyclerView.isVisible = true
                }
            }
            adapter.submitList(it)
        }
    }
    class Handler(private val viewModel: SearchViewModel) : ItemHandler {
        override fun onClickFavorite(item: ListItem) {
            viewModel.toggleFavorite(item)
        }
    }
}